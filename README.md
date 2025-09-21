# Clínica Oasis - App de Gestión de Citas Médicas

## Descripción
Clínica Oasis "Salud y Bienestar" es una aplicación móvil para la gestión de citas médicas. Permite a los pacientes:

- Agendar citas con doctores según consultorios y horarios disponibles.
- Guardar información médica personal (tipo de sangre, alergias, enfermedades, peso y altura).
- Recibir notificaciones de confirmación de citas por correo electrónico.
- Consultar comunicados importantes del consultorio.

La app está diseñada con una interfaz simple y amigable, utilizando colores claros y tipografía legible.

## Tecnologías utilizadas
- **Lenguaje:** Java
- **Entorno de desarrollo:** Android Studio
- **Base de datos:** Firebase
- **Compatibilidad:** Android 7.0+ (API 24+)

## Estructura del proyecto
com.example.clinicaoasis/
├── AgendarCitaActivity.java
├── CitaActivity.java
├── DatosMedicosActivity.java
├── ComunicadosActivity.java
├── SQLiteHelper.java
└── res/
├── layout/
│ ├── activity_agendar_cita.xml
│ ├── activity_cita.xml
│ ├── activity_datos_medicos.xml
│ ├── activity_comunicados.xml
│ └── item_comunicado.xml
└── values/
├── colors.xml
├── strings.xml
└── styles.xml

## Instalación

Clonar el repositorio:
git clone https://github.com/tu_usuario/clinica-oasis.git

Abrir el proyecto en Android Studio.
Sincronizar Gradle y ejecutar la app en un emulador o dispositivo físico.
