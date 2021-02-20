# [CRUD generator](https://docs.nestjs.com/recipes/crud-generator)

`cats`모듈은 `CRUD generator`로 자동생성된 예제입니다.

## [Generating a new resource](https://docs.nestjs.com/recipes/crud-generator#generating-a-new-resource)

```
$ nest g resource
```

## 생성된 파일 목록

```
./src/cats
├── README.md
├── cats.controller.spec.ts
├── cats.controller.ts
├── cats.module.ts
├── cats.service.spec.ts
├── cats.service.ts
├── dto
│   ├── create-cat.dto.ts
│   └── update-cat.dto.ts
└── entities
    └── cat.entity.ts
```

## 선택 가능한 사항

```
? What name would you like to use for this resource (plural, e.g., "users")? test
? What transport layer do you use? (Use arrow keys)
❯ REST API
  GraphQL (code first)
  GraphQL (schema first)
  Microservice (non-HTTP)
  WebSockets
```
