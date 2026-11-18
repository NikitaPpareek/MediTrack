# API examples

Run the backend first on `http://localhost:8080`.

## Create a patient

```bash
curl -X POST http://localhost:8080/api/patients \
  -H 'Content-Type: application/json' \
  -d '{"fullName":"Aanya Sharma","dateOfBirth":"1998-04-12","gender":"FEMALE","email":"aanya.demo@example.com","phone":"9000000000","notes":"Synthetic demo record"}'
```

## Search patients

```bash
curl 'http://localhost:8080/api/patients?q=aanya'
```

## Read dashboard metrics

```bash
curl http://localhost:8080/api/reports/summary
```
