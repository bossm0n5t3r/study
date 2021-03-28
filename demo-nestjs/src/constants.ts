import { User } from './users/entities/user.entity';

export const DATABASE = {
  LOCAL_HOST: '127.0.0.1',
  MARIA_DB: {
    DEFAULT_PORT: 3306,
    ENTITIES: [User],
  },
  MONGO_DB: {
    DEFAULT_PORT: 27017,
    ENTITIES: [],
  },
};
