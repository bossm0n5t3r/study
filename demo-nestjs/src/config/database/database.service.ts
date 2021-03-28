import * as dotenv from 'dotenv';
import * as fs from 'fs';
import { DATABASE } from '../../constants';

export interface DatabaseData {
  TYPE: 'mariadb' | 'mongodb';
  HOST: string;
  NAME: string;
  PORT: number;
  USERNAME: string;
  PASSWORD: string;
}

export class DatabaseService {
  private vars: DatabaseData;

  constructor(dbName: string) {
    const environment = process.env.NODE_ENV || 'development';
    const data: any = dotenv.parse(
      fs.readFileSync(`${environment}.${dbName}.env`),
    );

    data.HOST = data.HOST !== undefined ? data.HOST : DATABASE.LOCAL_HOST;

    const defaultPort =
      data.TYPE === 'mariadb'
        ? DATABASE.MARIA_DB.DEFAULT_PORT
        : DATABASE.MONGO_DB.DEFAULT_PORT;

    data.PORT = !isNaN(data.PORT) ? parseInt(data.PORT) : defaultPort;

    this.vars = data as DatabaseData;
  }

  read(): DatabaseData {
    return this.vars;
  }
}
