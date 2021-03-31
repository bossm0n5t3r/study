# demo-nestjs

[NestJS](https://nestjs.com/)를 연습하는 공간입니다.

[NestJS docs](https://docs.nestjs.com/)의 순서대로 기록을 남길 예정입니다.

## Description

[Nest](https://github.com/nestjs/nest) framework TypeScript starter repository.

## Constants 처리

공통적으로 사용되는 `Constants`값은 현재 [constants.ts](./src/constants.ts)파일안에 구성되어 있습니다.

### DB 구성

`mariaDB`, `mongoDB` 모두 적용된 상태입니다.

각 DB 설정은 `.env` 파일의 값으로 들어가 있습니다.

[database.module.ts](src/config/database/database.module.ts)에서 `import` 부분으로 `Database` 커넥션을 처리하며,

```ts
@Global()
@Module({
  imports: [DatabaseOrmModule('mariadb'), DatabaseOrmModule('mongodb')],
})
export class DatabaseModule {}
```

[database.service.ts](./src/config/database/database.service.ts)에서 해당하는 `dbName`으로 각 `.env`파일을 읽어서

각 `Database`에 맞게끔 연결해줍니다.

`mariaDB`는 `users` module을, `mongoDB`는 `tags` module에 대해서 현재 적용되어 있습니다.

## Installation

```bash
$ yarn install
```

## Running the app

```bash
# development
$ yarn run start

# watch mode
$ yarn run start:dev

# production mode
$ yarn run start:prod
```

## Test

```bash
# unit tests
$ yarn run test

# e2e tests
$ yarn run test:e2e

# test coverage
$ yarn run test:cov
```

## License

Nest is [MIT licensed](LICENSE).
