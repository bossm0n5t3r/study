import {
  Column,
  CreateDateColumn,
  Entity,
  PrimaryGeneratedColumn,
  UpdateDateColumn,
} from 'typeorm';

@Entity({
  name: 'user',
})
export class User {
  @PrimaryGeneratedColumn({
    name: 'id',
  })
  id: number;

  @Column({
    type: 'varchar',
    name: 'first_name',
    length: 255,
  })
  firstName: string;

  @Column({
    type: 'varchar',
    name: 'last_name',
    length: 255,
  })
  lastName: string;

  @Column({
    name: 'is_active',
  })
  isActive: boolean;

  @CreateDateColumn({
    name: 'created_at',
  })
  createdAt: string;

  @UpdateDateColumn({
    name: 'updated_at',
  })
  updatedAt: string;
}
