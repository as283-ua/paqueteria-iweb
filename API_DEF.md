# Envíos API
## POST. Tienda crea envío.
Ruta: POST `/envios/`

Body:

```
{
  "peso": int,
  "observaciones": string,
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
  "id": int
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
Ruta: GET `/envios/tarifas?peso=xxx&cp=xxxxx&bultos=xx`

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
Ruta: POST `/envios/{id}/historico/cancelar`

Parametros:
- id: `int`. Id del envío a cancelar.
- header `authorization`, `ApiKey: string`

Status:
- 204: En el histórico del envío se añade un nuevo estado: "Cancelado".
- 400: Id no existe.
- 401: API key no identificada o vacía.


## GET. Mostrar datos e histórico de todos envíos de tienda
Ruta: GET `/envios/`

Body:
```
{
  "fechaInicio": timestamp | null,
  "fechaFin": timestamp | null
}
```
Parametros:
- header `authorization`, `ApiKey: string`. En base a la API key se identifica la tienda y se devuelven los envíos de esta misma.

Status:
- 200: Listado de envios e historicos de la tienda entre fechas especificadas:

```
[
  {
    "id": int,
    "historico": [
      {
        "estado": string,
        "timestamp": timestamp,
        "observaciones": string
      },
      ...
    ],
    "peso": int,
    "precio": int,
    "observaciones": string
  },
  ...
]
```

- 401: API key no identificada o vacía.
## GET. Mostrar datos e histórico de envío (público)
Ruta: GET `/envios/{id}`
Parametros:
- id: `int`. Id del envío.

Status:
- 200: Devuelve datos básicos de envío:

```
{
  "peso": string,
  "historico": [
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
