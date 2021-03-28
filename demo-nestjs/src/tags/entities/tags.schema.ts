import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

export type TagsDocument = Tags & Document;

@Schema()
export class Tags {
  @Prop()
  name: string;

  @Prop([String])
  items: string[];
}

export const TagsSchema = SchemaFactory.createForClass(Tags);
