# Especificación de la API: Servicio de paquetería

El propósito de esta API es gestionar envíos. Ofrece distintos end-points a las tiendas que emplearán
este servicio para gestionar sus envíos.

Las operaciones disponibles son:
- Crear envío.
- Realizar consulta de que valdría un envío según sus especificaciones. El objetivo es dar la opción
    a la tienda de mostrar al cliente el precio del envío antes de realizarlo.
- Cancelar envío.
- Mostrar datos e histórico de todos los envíos de la tienda entre dos fechas.
- Mostrar datos e histórico de un envío concreto. El objetivo de este end-point es que la tienda
    pueda mostrar al cliente el estado de su envío sin necesidad de tener que acceder a nuestra página,
    mostrándolo directamente en la web de la tienda.

## POST. Tienda crea envío.
Ruta: POST `/api/envios/`

Body:

```
{
  "peso": int,
  "observaciones": string,
  "bultos": int,
  "destino": {
    "codigoPostal": string,
    "localidad": string,
    "provincia": string,
    "numero": int,
    "planta": string,
    "calle": string,
    "nombre": string,
    "telefono": int
  }
}
```

Params: header `authorization`, `ApiKey: string`

Devuelve:

```
{
  "codigo": string
}
```

Status: 
- 201: se crea correctamente.
- 400: formato de datos incorrecto.
  - Body:
    ```
    {
      "errors":{
        string,
        ...  
      }
    }
    ```
- 401: API key no identificada o vacía.

## GET. Consultar precio de envío
Ruta: GET `/api/envios/tarifas?peso=xxx&cp=xxxxx&bultos=xx`

Parametros:
- peso: `int`. Peso del envío.
- cp: `string`. Código postal de destinatario.
- bultos: `int`. Bultos de envío.
- Otras tarifas que se vayan incluyendo en el futuro se especificaran aquí.
- header `authorization`, `ApiKey: string`

Devuelve:

```
{
  "precio": int,
  "tarifas": [
    {
      "nombre": string,
      "coste": int,
      "cantidad": int
    },
    ...
  ]
}
```

Status:
- 200: Parámetros correctos. Devuelve body con el formato anterior.
- 400: Parámetros incorrectos.
- 401: API key no identificada o vacía.

## POST. Cancelar envío
Ruta: POST `/api/envios/{codigo}/historico/cancelar`

Body [opcional]:
```
{
  "observaciones": string
}
```

Parametros:
- codigo: `string`. Código del envío a cancelar.
- header `Authorization`, `ApiKey: string`

Status:
- 204: En el histórico del envío se añade un nuevo estado: "Cancelado".
- 400: Id no existe.
- 400: Paquete ya enviado.
- 401: API key no identificada o vacía.


## GET. Mostrar datos e histórico de todos envíos de tienda
Ruta: GET `/api/envios/`

Body:
```
{
  "fechaInicio": timestamp | null,
  "fechaFin": timestamp | null
}
```
Parametros:
- header `authorization`, `ApiKey: string`. En base a la API key se identifica la tienda y se devuelven los envíos de esta misma.
- fechaInicio: `timestamp`. Fecha de inicio de búsqueda. Se aplica a la fecha de creación del pedido (estado "En almacén"). Si no se especifica no se aplica este filtro de fecha mínima.
- fechaInicio: `timestamp`. Fecha de fin de búsqueda. Se aplica a la fecha de creación del pedido (estado "En almacén"). Si no se especifica no se aplica este filtro de fecha máxima.

- Status:
- 200: Listado de envios e historicos de la tienda entre fechas especificadas:

```
[
  {
    "codigo": string,
    "peso": int,
    "precio": int,
    "historicos": [
      {
        "estado": string,
        "timestamp": timestamp,
        "observaciones": string
      },
      ...
    ]
  },
  ...
]
```

- 401: API key no identificada o vacía.
## GET. Mostrar datos e histórico de envío (público)
Ruta: GET `/api/envios/{codigo}`
Parametros:
- codigo: `string`. Código UUID que identifica al envío.

Status:
- 200: Devuelve datos básicos de envío:

```
{
  "codigo": string,
  "peso": string,
  "bultos": int,
  "historicos": [
    {
      "estado": string,
      "timestamp": timestamp,
      "observaciones": string
    },
    ...
  ]
}
```

- 400: Id no existe.
