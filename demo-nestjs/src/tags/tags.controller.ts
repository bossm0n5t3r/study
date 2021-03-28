import { Controller, Get, Post, Body } from '@nestjs/common';
import { TagsService } from './tags.service';
import { CreateTagsDto } from './dto/create-tags.dto';

@Controller('tags')
export class TagsController {
  constructor(private readonly tagsService: TagsService) {}

  @Get()
  helloTags() {
    return 'Hello, this is a TagsController';
  }

  @Post('/create')
  create(@Body() createTagsDto: CreateTagsDto) {
    return this.tagsService.create(createTagsDto);
  }

  @Get('/findAll')
  findAll() {
    return this.tagsService.findAll();
  }
}
