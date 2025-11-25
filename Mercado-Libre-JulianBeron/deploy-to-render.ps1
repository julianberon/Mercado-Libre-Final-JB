# Script para preparar y commitear cambios para Render
Write-Host "🚀 Preparando proyecto para Render..." -ForegroundColor Cyan
Write-Host ""

# Verificar que estamos en un repositorio Git
if (-Not (Test-Path ".git")) {
    Write-Host "❌ Error: No estás en un repositorio Git" -ForegroundColor Red
    Write-Host "Ejecuta primero: git init" -ForegroundColor Yellow
    exit 1
}

Write-Host "✅ Repositorio Git detectado" -ForegroundColor Green

# Mostrar estado actual
Write-Host ""
Write-Host "📋 Estado actual del repositorio:" -ForegroundColor Cyan
git status --short

# Confirmar con el usuario
Write-Host ""
$confirmation = Read-Host "¿Deseas agregar todos los cambios y hacer commit? (s/n)"

if ($confirmation -ne 's' -and $confirmation -ne 'S') {
    Write-Host "❌ Operación cancelada" -ForegroundColor Yellow
    exit 0
}

# Agregar todos los cambios
Write-Host ""
Write-Host "📦 Agregando archivos..." -ForegroundColor Cyan
git add .

# Mostrar lo que se va a commitear
Write-Host ""
Write-Host "📝 Archivos a commitear:" -ForegroundColor Cyan
git status --short

# Hacer commit
Write-Host ""
$commitMessage = Read-Host "Ingresa el mensaje de commit (Enter para usar el predeterminado)"

if ([string]::IsNullOrWhiteSpace($commitMessage)) {
    $commitMessage = "Configure project for Render deployment with Docker"
}

git commit -m "$commitMessage"

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "✅ Commit exitoso!" -ForegroundColor Green

    # Preguntar si desea hacer push
    Write-Host ""
    $pushConfirmation = Read-Host "¿Deseas hacer push al repositorio remoto? (s/n)"

    if ($pushConfirmation -eq 's' -or $pushConfirmation -eq 'S') {
        Write-Host ""
        Write-Host "🚀 Haciendo push..." -ForegroundColor Cyan

        # Obtener la rama actual
        $currentBranch = git branch --show-current

        git push origin $currentBranch

        if ($LASTEXITCODE -eq 0) {
            Write-Host ""
            Write-Host "✅ Push exitoso!" -ForegroundColor Green
            Write-Host ""
            Write-Host "🎉 ¡Proyecto listo para desplegar en Render!" -ForegroundColor Cyan
            Write-Host ""
            Write-Host "Próximos pasos:" -ForegroundColor Yellow
            Write-Host "1. Ve a https://dashboard.render.com/" -ForegroundColor White
            Write-Host "2. Click en 'New +' → 'Web Service'" -ForegroundColor White
            Write-Host "3. Conecta tu repositorio" -ForegroundColor White
            Write-Host "4. Render detectará automáticamente el archivo render.yaml" -ForegroundColor White
            Write-Host "5. Click en 'Create Web Service'" -ForegroundColor White
            Write-Host ""
            Write-Host "📖 Para más detalles, consulta: DEPLOY_INSTRUCTIONS.md" -ForegroundColor Cyan
        } else {
            Write-Host ""
            Write-Host "❌ Error al hacer push" -ForegroundColor Red
            Write-Host "Verifica que tengas configurado el repositorio remoto:" -ForegroundColor Yellow
            Write-Host "  git remote -v" -ForegroundColor White
            Write-Host ""
            Write-Host "Si no tienes un remoto configurado, agrégalo:" -ForegroundColor Yellow
            Write-Host "  git remote add origin <URL_DE_TU_REPO>" -ForegroundColor White
        }
    } else {
        Write-Host ""
        Write-Host "ℹ️ Puedes hacer push manualmente después:" -ForegroundColor Cyan
        Write-Host "  git push origin main" -ForegroundColor White
    }
} else {
    Write-Host ""
    Write-Host "❌ Error al hacer commit" -ForegroundColor Red
    Write-Host "Verifica que tengas cambios para commitear" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "Presiona Enter para salir..."
Read-Host

