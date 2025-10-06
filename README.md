# Rick and Morty Characters App

Una aplicación Android que muestra personajes de Rick and Morty usando la API pública.

## 🚀 Configuración del Proyecto

### Prerrequisitos
- Android Studio Arctic Fox o superior
- JDK 11 o superior
- Android SDK API 29 o superior

### Instalación

1. **Clonar el repositorio**
   ```bash
   git clone <repository-url>
   cd Akkodis
   ```

2. **Configurar Android SDK**
   - Abrir Android Studio
   - Ir a `File > Project Structure > SDK Location`
   - Verificar que el Android SDK esté configurado correctamente
   - O crear un archivo `local.properties` en la raíz del proyecto:
   ```properties
   sdk.dir=/path/to/your/android/sdk
   ```

3. **Sincronizar el proyecto**
   ```bash
   ./gradlew clean
   ./gradlew build
   ```

4. **Ejecutar tests**
   ```bash
   ./gradlew test
   ```

## 🏗️ Arquitectura

El proyecto sigue Clean Architecture con las siguientes capas:

- **App**: Presentación (UI, ViewModels)
- **Domain**: Lógica de negocio (Use Cases, Models)
- **Data**: Fuentes de datos (API, Repository)

## 🧪 Testing

### Ejecutar todos los tests
```bash
./gradlew test
```

### Ejecutar tests de un módulo específico
```bash
./gradlew :data:test
./gradlew :domain:test
./gradlew :app:test
```

### Ejecutar tests con reportes
```bash
./gradlew test jacocoTestReport
```

## 🔧 Tecnologías Utilizadas

- **Kotlin** - Lenguaje de programación
- **Jetpack Compose** - UI moderna
- **Retrofit** - Cliente HTTP
- **Koin** - Inyección de dependencias
- **Coil** - Carga de imágenes
- **JUnit 5** - Testing framework
- **MockK** - Mocking library

## 📱 Funcionalidades

- ✅ Lista de personajes con scroll infinito
- ✅ Búsqueda de personajes por nombre
- ✅ Detalles del personaje
- ✅ Manejo robusto de errores con SafeApiCall
- ✅ Arquitectura limpia y modular
- ✅ Tests unitarios completos

## 🐛 Solución de Problemas

### Error: "SDK location not found"
Crear archivo `local.properties` en la raíz del proyecto:
```properties
sdk.dir=/path/to/your/android/sdk
```

### Error: "Tests not running"
```bash
./gradlew clean
./gradlew test
```

### Error: "Build failed"
```bash
./gradlew clean
./gradlew build --refresh-dependencies
```

## 📄 Licencia

Este proyecto es para fines educativos y de demostración.
