import { Injectable } from '@nestjs/common';
import { CreateCatDto } from '../dto/Cats/create-cat.dto';

@Injectable()
export class CatsService {
  createCats(createCatDto: CreateCatDto): string {
    const name = createCatDto.name;
    const age = createCatDto.age.toString();
    const breed = createCatDto.breed;
    return `name:${name}, age:${age}, breed:${breed}`;
  }
}
