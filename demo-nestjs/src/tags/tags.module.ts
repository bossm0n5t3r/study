import { Module } from '@nestjs/common';
import { TagsService } from './tags.service';
import { TagsController } from './tags.controller';
import { MongooseModule } from '@nestjs/mongoose';
import { Tags, TagsSchema } from './entities/tags.schema';

@Module({
  imports: [
    MongooseModule.forFeature([{ name: Tags.name, schema: TagsSchema }]),
  ],
  controllers: [TagsController],
  providers: [TagsService],
})
export class TagsModule {}
