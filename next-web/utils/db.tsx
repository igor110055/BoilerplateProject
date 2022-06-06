import promise from 'bluebird'; // best promise library today
import pgPromise from 'pg-promise';
import pg from 'pg-promise/typescript/pg-subset';
const db = (): pgPromise.IDatabase<{}, pg.IClient> => { 

    const initOptions = {
        promiseLib: promise // overriding the default (ES6 Promise);
    };

    const pgp = pgPromise(initOptions)
    //`postgres://${config.user}:${config.password}@${config.host}:${config.port}/${config.database}`
    const cn = `postgres://${process.env.DB_USER}:${process.env.DB_PASSWORD}@${process.env.DB_HOST}:${process.env.DB_PORT}/${process.env.DB}`
    const db2=  pgp(cn)
    return db2
    
} 
export default (db)()

