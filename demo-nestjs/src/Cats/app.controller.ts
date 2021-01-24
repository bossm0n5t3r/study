import { Controller, Get } from '@nestjs/common';
import { AppService } from '../app.service';

@Controller('Cats')
export class CatsController {
  constructor(private readonly appService: AppService) {}

  @Get()
  getHello(): string {
    return this.appService.getMeow();
  }
}
