# Cómo arrancar la aplicación con Docker

## Con BD H2

Hay una [imagen docker](https://hub.docker.com/r/as283/paqueteria-iweb) disponible.

`docker run --rm -d -p <puerto>:8080 as283/paqueteria-iweb`

Esto lanza la versión de desarrollo con datos de prueba. Los importantes son los de las tiendas, que podeis usar para 
realizar la peticiones a la API REST. La tienda1, con email "tienda1@ua" y contraseña "123", y la tienda2, con email 
"tienda2@ua" y contraseña "123". Estos datos los podeis consultar en el fichero `InitDbDevService.java` en la carpeta de
servicios.

Las API keys usadas para estos dos usuarios son "123456789" y "1234567890" respectivamente. Estos se deben usar en las
peticiones a la API REST añadiendolo en el header "Authorization" como valor. La API key es única e identifica a la 
tienda.

La documentación de la API REST está `API_DEF.md` en este mismo directorio.

## Con BD postgres

- Crear Network Docker:
`docker network create network-iweb`

- Crear BD Postgres:

`docker run --network network-iweb --network-alias postgres -d -p 5432:5432 --name postgres-iweb -e POSTGRES_USER=paqueteria -e POSTGRES_PASSWORD=paqueteria -e 
POSTGRES_DB=paqueteria postgres:13`

- Arrancar la aplicación:
`docker run --rm --network network-iweb -p 8080:8080 as283/paqueteria-iweb --spring.profiles.active=postgres --POSTGRES_HOST=postgres`

Hace falta dar de alta a las tiendas con el usuario "admin@ua", contraseña "admin123".
