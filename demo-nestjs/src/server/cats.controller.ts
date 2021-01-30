import {
  Controller,
  Get,
  Header,
  HttpCode,
  Param,
  Post,
  Query,
  Redirect,
  Req,
} from '@nestjs/common';
import { AppService } from '../app.service';
import { Request } from 'express';
import { Body } from '@nestjs/common';
import { CreateCatDto } from '../dto/Cats/create-cat.dto';
import { CatsService } from '../service/cats.service';

@Controller('Cats')
export class CatsController {
  constructor(
    private readonly appService: AppService,
    private readonly catsService: CatsService,
  ) {}

  @Post('CreateCatWithOutDto')
  createCatWithOutDto(): string {
    return 'This action adds a new cat';
  }

  @Get('GetMeow')
  getMeow(): string {
    return this.appService.getMeow();
  }

  @Get('RequestObject')
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
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

  @Post('Header')
  @Header('Cache-Control', 'none')
  @Header('Content-Type', 'application/json')
  header() {
    return 'This action adds a new cat';
  }

  @Get('docs')
  @Redirect('https://docs.nestjs.com', 302)
  getDocs(@Query('version') version) {
    if (version && version === '5') {
      return { url: 'https://docs.nestjs.com/v5/' };
    }
  }

  @Get('params/:id')
  findOne(@Param('id') id: string): string {
    // console.log(id);
    return `This action returns a #${id} cat`;
  }

  @Get('async')
  async findAll(): Promise<any[]> {
    return [];
  }

  @Post('CreateCatWithDto')
  async createCatWithDto(@Body() createCatDto: CreateCatDto): Promise<string> {
    const result: string = this.catsService.createCats(createCatDto);
    return result;
  }
}
