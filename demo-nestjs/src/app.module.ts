import { Module } from '@nestjs/common';
import { Connection } from 'typeorm';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { CatsModule } from './cats/cats.module';
import { DatabaseModule } from './config/database/database.module';
import { UserHttpModule } from './users/users-http.module';

@Module({
  imports: [DatabaseModule, CatsModule, UserHttpModule],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {
  constructor(private connection: Connection) {}
}
