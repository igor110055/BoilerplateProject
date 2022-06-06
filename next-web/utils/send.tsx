import React,{useState,useRef} from 'react';
import axios, { AxiosResponse } from 'axios';
import {Session} from 'next-auth/core/types';

export interface IParams extends Record<string, unknown>  {
    brRq:string ,
    brRs:string ,
}


export interface ISend {
    BR: string;
    PARAM: IParams;
}



const send = async (br:string, params:IParams) => {
    //console.log(br)
    if(typeof window === 'undefined') {
        throw new Error('send 함수는 client에서 호출되어야 합니다.')
    }

    const url = `${process.env.NEXT_PUBLIC_URL}api/${br}`
    //console.log({br})
    //console.log({url})    
    //console.log(params)
    //console.log(JSON.stringify(params))

    const c:AxiosResponse = await axios.post(
        url,
        JSON.stringify(params),
        {
            method: "POST",
            headers: {
            "Content-Type": "application/json",
        }
    });
    const data = await c.data;


    return new Promise((resolve, reject) => { // !
        let result;
        try {
            if(data.status==='E'){
                throw data
            }
          result = data.data;
        } catch (err) {
          //console.log('status=>error')
          console.log({err})
          reject(err);  // ! return err; => reject(err);
          //console.log(err)

        }
          //console.log('status=>ok')
          //console.log(result)
          resolve(result); // ! return result; => resolve(result);
      })
}
export default send



export  const send_server = async (br:string, params:IParams , session: Session | null) => {
    console.log(br)
    if(typeof window !== 'undefined') {
        throw new Error('send_server 함수는 server에서 호출되어야 합니다.')
    }

    params=setSessionApi(params,session)

    const url = `${process.env.NEXT_PUBLIC_URL}api/${br}`
    //console.log({br})
    //console.log({url})    
    //console.log(params)
    //console.log(JSON.stringify(params))

    const c:AxiosResponse = await axios.post(
        url,
        JSON.stringify(params),
        {
            method: "POST",
            headers: {
            "Content-Type": "application/json",
        }
    });
    const data = await c.data;
    
    return new Promise((resolve, reject) => { // !
        let result;
        try {
            //console.log("aaaaaaaaaa")
            //console.log(data)
            if(data.status==='E'){
                throw data
            }
          result = data.data;
        } catch (err) {
          //console.log('status=>error')
            //console.log("bbbbb")
            console.log(err)
          reject(err);  // ! return err; => reject(err);
          //console.log(err)
        }
          //console.log('status=>ok')
          //console.log(result)
          resolve(result); // ! return result; => resolve(result);
      })
}



export const send_sql = async (br:string,params:Record<string, unknown>) => {
    //console.log(br)
    if(typeof window === 'undefined') {
        throw new Error('send 함수는 client에서 호출되어야 합니다.')
    }

    const url = `${process.env.NEXT_PUBLIC_URL}api/sql/${br}`
    //console.log({br})
    //console.log({url})    
    //console.log(params)
    //console.log(JSON.stringify(params))

    const c:AxiosResponse = await axios.post(
        url,
        JSON.stringify(params),
        {
            method: "POST",
            headers: {
            "Content-Type": "application/json",
        }
    });
    const data = await c.data;
    console.log({data})


    return new Promise((resolve, reject) => { // !
        let result;
        try {
            if(data.status==='E'){
                throw data
            }
          result = data.data;
        } catch (err) {
          //console.log('status=>error')
          reject(err);  // ! return err; => reject(err);
          //console.log(err)
        }
          //console.log('status=>ok')
          //console.log(result)
          resolve(result); // ! return result; => resolve(result);
      })
}

const setSessionApi = (params:IParams,session: Session|null):any => {
     let brRq:string = params.brRq;
     const arr_brRq:string[] = brRq.split(",");
      
     let isSession = false;
     for (let i = 0; i < arr_brRq.length; i++) {
         let tmp:string = arr_brRq[i];
         if(tmp=="SESSION") {
             isSession=true;
         }
     }
     
     if(isSession ==true){
         if(session!=null){
             if(session.user){
                let tmp = session.user as any;
                params["SESSION"]=[{
                    USER_UID: tmp.user_uid,
                    NICK_NM: tmp.nick_nm,
                    EMAIL: tmp.email,            
                }]
             }            
         } 
     }
     return params
 }
