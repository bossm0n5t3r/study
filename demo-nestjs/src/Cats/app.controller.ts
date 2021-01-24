import { Controller, Get, HttpCode, Post, Req } from '@nestjs/common';
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
  requestObject(@Req() request: Request): string {
    return 'This action returns all cats';
  }

  @Get('ab*cd')
  wildcard() {
    return 'This route uses a wildcard';
  }

  @Post('StatusCode')
  @HttpCode(204)
  statusCode(): string {
    return 'This action adds a new cat';
  }
}
