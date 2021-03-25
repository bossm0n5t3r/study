import { Module } from '@nestjs/common';
import { UsersController } from './users.controller';
import { UsersEntityModule } from './users-entity.module';
import { UsersService } from './users.service';

@Module({
  imports: [UsersEntityModule],
  providers: [UsersService],
  controllers: [UsersController],
})
export class UsersModule {}
