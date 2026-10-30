# Prueba local: crea registros identificados y elimina solo esos registros al terminar.
# Requiere PowerShell 7, backend iniciado y los usuarios iniciales del SQL.
$ErrorActionPreference = 'Stop'
$base = 'http://localhost:8080'
function Api($metodo, $ruta, $cabeceras, $datos = $null) {
    $opciones = @{ Uri = "$base$ruta"; Method = $metodo; Headers = $cabeceras; TimeoutSec = 20 }
    if ($null -ne $datos) {
        $opciones.ContentType = 'application/json'
        $opciones.Body = $datos | ConvertTo-Json -Depth 5
    }
    Invoke-RestMethod @opciones
}
function Comprobar($condicion, $mensaje) {
    if (!$condicion) { throw "FALLO: $mensaje" }
    Write-Output "OK: $mensaje"
}
$admin = Api Post '/auth/login' @{} @{email='admin@biblioteca.com';password='admin123'}
$lector = Api Post '/auth/login' @{} @{email='lector@biblioteca.com';password='lector123'}
$ha = @{Authorization="Bearer $($admin.token)"}
$hl = @{Authorization="Bearer $($lector.token)"}
Comprobar ($admin.rol -eq 'ADMIN' -and $lector.rol -eq 'LECTOR') 'Login de ambos roles'
$r = Invoke-WebRequest "$base/libros" -SkipHttpErrorCheck -TimeoutSec 20
Comprobar ($r.StatusCode -eq 401) 'Sin token: 401'
$r = Invoke-WebRequest "$base/usuarios" -Headers $hl -SkipHttpErrorCheck -TimeoutSec 20
Comprobar ($r.StatusCode -eq 403) 'LECTOR no consulta usuarios: 403'
$r = Invoke-WebRequest "$base/libros" -Headers @{Authorization='Bearer invalido'} -SkipHttpErrorCheck -TimeoutSec 20
Comprobar ($r.StatusCode -eq 401) 'Token invalido: 401'
$marca = 'Prueba-' + [Guid]::NewGuid().ToString('N').Substring(0,10)
$autorId = $null; $libroId = $null; $usuarioId = $null; $prestamoIds = @()
try {
    $autor = @{nombre=$marca;apellido='Temporal';nacionalidad='Colombiana';fechaNacimiento='1980-01-01'}
    Api Post '/autores' $ha $autor
    $autorId = (Api Get '/autores?page=0&size=1000' $ha).content | Where-Object nombre -eq $marca | Select-Object -ExpandProperty id
    Comprobar ($null -ne $autorId) 'Crear autor'
    $autor.id = $autorId; $autor.apellido = 'Actualizado'
    Api Post '/autores' $ha $autor
    Comprobar ((Api Get "/autores/$autorId" $ha).apellido -eq 'Actualizado') 'Editar autor'
    $libro = @{titulo=$marca;isbn=$marca;anio=2020;autorId=$autorId}
    Api Post '/libros' $ha $libro
    $libroId = (Api Get "/libros/buscar?texto=$marca" $ha).content[0].id
    Comprobar ($null -ne $libroId) 'Crear y buscar libro'
    $libro.id = $libroId; $libro.anio = 2021
    Api Post '/libros' $ha $libro
    Comprobar ((Api Get "/libros/$libroId" $hl).anio -eq 2021) 'Editar libro y leer como LECTOR'
    $usuario = @{nombre=$marca;username=$marca;email="$marca@example.test";password='ClaveTemporal123';rol='LECTOR';estado='ACTIVO'}
    Api Post '/usuarios' $ha $usuario
    $usuarioId = (Api Get "/usuarios/buscar?texto=$marca" $ha).content[0].id
    $usuario.id = $usuarioId; $usuario.nombre = "$marca Editado"
    Api Post '/usuarios' $ha $usuario
    $segundo = Api Post '/auth/login' @{} @{email=$usuario.email;password=$usuario.password}
    Comprobar ($segundo.id -eq $usuarioId) 'Crear/editar usuario y verificar BCrypt con login'
    $prestamo = @{libroId=$libroId;usuarioId=$usuarioId;fechaPrestamo='2026-10-29';fechaDevolucion=$null;devuelto=$false}
    Api Post '/prestamos' $hl $prestamo
    $propio = (Api Get '/prestamos?page=0&size=1000' $ha).content | Where-Object libroId -eq $libroId | Select-Object -First 1
    $prestamoIds += $propio.id
    Comprobar ($propio.usuarioId -eq $lector.id) 'Backend asigna al lector autenticado aunque envie otro usuarioId'
    Api Post '/prestamos' $ha $prestamo
    $ajeno = (Api Get '/prestamos?page=0&size=1000' $ha).content | Where-Object { $_.libroId -eq $libroId -and $_.usuarioId -eq $usuarioId } | Select-Object -First 1
    $prestamoIds += $ajeno.id
    $prestamo.id = $ajeno.id; $prestamo.devuelto = $true
    $r = Invoke-WebRequest "$base/prestamos" -Method Post -Headers $hl -ContentType application/json -Body ($prestamo | ConvertTo-Json) -SkipHttpErrorCheck -TimeoutSec 20
    Comprobar ($r.StatusCode -eq 403) 'LECTOR no devuelve prestamo ajeno: 403'
    $prestamo.id = $propio.id
    Api Post '/prestamos' $hl $prestamo
    $devuelto = Api Get "/prestamos/$($propio.id)" $ha
    Comprobar ($devuelto.devuelto -and $devuelto.usuarioId -eq $lector.id) 'Devolver prestamo propio preserva propietario'
    $r = Invoke-WebRequest "$base/prestamos/$($propio.id)" -Method Delete -Headers $hl -SkipHttpErrorCheck -TimeoutSec 20
    Comprobar ($r.StatusCode -eq 403) 'LECTOR no elimina prestamos: 403'
    $r = Invoke-WebRequest "$base/autores" -Method Post -Headers $ha -ContentType application/json -Body '{"nombre":"","apellido":""}' -SkipHttpErrorCheck -TimeoutSec 20
    Comprobar ($r.StatusCode -eq 400) 'Validacion de DTO: 400'
    Comprobar ((Api Get '/libros?page=0&size=1' $ha).size -eq 1) 'Paginacion'
} finally {
    foreach ($id in $prestamoIds) { if ($null -ne $id) { Api Delete "/prestamos/$id" $ha } }
    if ($null -ne $libroId) { Api Delete "/libros/$libroId" $ha }
    if ($null -ne $usuarioId) { Api Delete "/usuarios/$usuarioId" $ha }
    if ($null -ne $autorId) { Api Delete "/autores/$autorId" $ha }
    Write-Output 'Limpieza terminada: eliminados solo los registros temporales de esta ejecucion.'
}
Comprobar ((Api Get "/libros/buscar?texto=$marca" $ha).totalElements -eq 0) 'Eliminar libro verificado'
