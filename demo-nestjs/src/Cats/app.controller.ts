import { Controller, Get, Post, Req } from '@nestjs/common';
import { AppService } from '../app.service';
import { Request } from 'express';

@Controller('Cats')
export class CatsController {
  constructor(private readonly appService: AppService) {}

  @Post()
  create(): string {
    return 'This action adds a new cat';
  }

  @Get()
  getMeow(): string {
    return this.appService.getMeow();
  }

  @Get('RequestObject')
  findAll(@Req() request: Request): string {
    return 'This action returns all cats';
  }
}
