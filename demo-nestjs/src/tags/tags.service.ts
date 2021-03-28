import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { CreateTagsDto } from './dto/create-tags.dto';
import { Tags, TagsDocument } from './entities/tags.schema';

@Injectable()
export class TagsService {
  constructor(@InjectModel(Tags.name) private tagsModel: Model<TagsDocument>) {}

  async create(createTagsDto: CreateTagsDto): Promise<Tags> {
    const createdTags = new this.tagsModel(createTagsDto);
    return createdTags.save();
  }

  async findAll() {
    return this.tagsModel.find().exec();
  }
}
