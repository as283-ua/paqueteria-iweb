# Envíos API
## POST. Tienda crea envío.
Ruta: POST `/envios/`

Body:

```json
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

```json
{
  "id": int
}
```

Status: 
- 201: se crea correctamente.
- 400: formato de datos incorrecto.
  - Body:
    ```json
    {
      errors:{
        string,
        ...  
      }
    }
    ```
- 401: API key no identificada o vacía.

## GET. Consultar precio de envío
Ruta: GET `/envios/tarifas?peso=xxx&cp=xxxxx&bultos=xx`

Parametros:
- peso: `int`. Peso del paquete.
- cp: `string`. Código postal de destinatario.
- bultos: `int`. Bultos de paquete.
- Otras tarifas que se vayan incluyendo en el futuro se especificaran aquí.
- header `authorization`, `ApiKey: string`

Devuelve:

```json
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

## ???. Cancelar envío
Ruta: UPDATE/DELETE `/envios/{id}`
Parametros:
- id: `int`. Id del paquete a cancelar.
- header `authorization`, `ApiKey: string`
Status: 204, 400


## GET. Mostrar datos e histórico de envío
Ruta: GET `/envios/{id}`
Parametros:
- id: `int`. Id del paquete.

Status:
- 200: Devuelve datos básicos de envío:

```json
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
