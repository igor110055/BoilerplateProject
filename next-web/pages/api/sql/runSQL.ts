// Next.js API route support: https://nextjs.org/docs/api-routes/introduction
import type { NextApiRequest, NextApiResponse } from 'next'
import db from '@/utils/db'
import pgPromise from 'pg-promise'
import pg from 'pg-promise/typescript/pg-subset'

type Data = {
  name: string
}

export default async function handler(req:NextApiRequest, res: NextApiResponse  ) {
    console.log(req.body)

    const sql = req.body.SQL
    if(sql === undefined){
        res.status(405).send({ 'message': 'SQL이 넘어오지 않았습니다.' })
        return
    }
    

    db.result(sql)
    .then(data => {
        // data = array of column descriptors
        console.log(data)
        res.status(200).json({
            status:'OK',
            data:data
        })
    }).catch((error) => {
        // error
        console.log(error.message);
        res.status(200).json({
            status:'E',
            data:error.message
        })
        
    })
}
