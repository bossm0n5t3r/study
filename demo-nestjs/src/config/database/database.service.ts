import * as dotenv from 'dotenv';
import * as fs from 'fs';

export interface DatabaseData {
  TYPE: 'mariadb' | 'mongodb';
  HOST?: string;
  NAME: string;
  PORT?: number;
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

    data.HOST = data.HOST !== null ? data.HOST : '127.0.0.1';
    data.PORT = data.PORT !== null ? parseInt(data.PORT) : 3306;

    this.vars = data as DatabaseData;
  }

  read(): DatabaseData {
    return this.vars;
  }
}
