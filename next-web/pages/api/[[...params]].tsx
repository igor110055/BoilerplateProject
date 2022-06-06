import type { NextApiRequest, NextApiResponse } from 'next'
import { env } from "process";
import { v4 as uuidv4 } from 'uuid';
import { getSession } from 'next-auth/react'

type res_data = {
    status : string
    data: any
    err_msg:string,
    code : string

}

export default async function handler(req:NextApiRequest, res: NextApiResponse  ) {
    const session = await getSession({req})
    //console.log({req})
    //console.log({session})

    const params = req.query.params
    if(params === undefined){
        res.status(405).send({ 'message': 'BR명이 넘어오지 않았습니다.' })
        return
    }
    const br=params[0]
    const body:any  = req.body    // json 으로 넘어올때 부터  object 객체이다. string으로 해도 안바뀐다.
    
    if(body === ""){
        res.status(405).send({ 'message': 'body 가 넘어오지 않았습니다.' })
        return
    }

    if(body.brRq === undefined){
        res.status(405).send({ 'message': 'brRq 속성 자체가 넘어오지 않았습니다.(값은 안 넘어와도 통과)' })
        return
    }

    if(body.brRs === undefined){
        res.status(405).send({ 'message': 'brRs 속성 자체가 넘어오지 않았습니다.(값은 안 넘어와도 통과)' })
        return
    }
    //console.log("aaa")
    const param_body=parserApi(br,body,session)
    //console.log(param_body)
    let url = `${process.env.BIZACTOR_API_URL}`
    const response = await fetch(url,{
        method:'POST',
        body: param_body,
        headers:{'Content-Type': 'application/json'}
        })
        //console.log("cccccccc")
        const data = await response.json()
        //console.log(JSON.stringify(data))        


        /*
        출력 양식을 여기서 정하자
        {
            status:"S|E"
            data: object
            code:"error코드"
            err_msg: "에러 메시지"
        }
        */
        let result : res_data = {status:"S",data:{},code:"",err_msg:"" }
        if(data.status=="BIZEXCEPTION"){
            result.status="E"
            result.data=""
            result.code=data.code
            result.err_msg=data.message
        } else if(data.status=="FAILED"){            
            result.status="E"
            result.data=""
            result.err_msg=data.message
        } else if(data.status=="SUCCESS"){      
            result.status="S"
            result.data=data.result      
            result.err_msg=""
        }

        res.status(200).json(result)
}

const parserApi = (br:string,body:any,session:any):any => {
   /*테스트는 postman으로 하자 curl 잘 안된다.
    BR_TEST   
   '{"brRq":"IN_PARAM", "brRs":"OUT_RESULT","IN_PARAM":[{"MSG":"TEST"}] }'
   이걸 아래와 같은 형식으로 바꾸어한다.
   {
            "actID":"BR_TEST"
            ,"inDTName":"IN_PARAM"
            ,"outDTName":"OUT_RESULT"
            ,"refDS":{"IN_PARAM":[
                                {"MSG":"TEST"}
                                ]
                    }
   }
   */
   const uuid=uuidv4(); // ⇨ '9b1deb4d-3b7d-4bad-9bdd-2b0d7b3dcb6d'
    let refDs : any = {};
    let brRq:string = body.brRq;
    const arr_brRq:string[] = brRq.split(",");

    
    let isSession = false;
    for (let i = 0; i < arr_brRq.length; i++) {
        let tmp:string = arr_brRq[i];
        let arr_input_param = body[tmp];
        refDs[tmp]=arr_input_param;

        if(tmp=="SESSION") {
            isSession=true;
        }
    }

    //클라이언트에 세션값이 안보이게 하는 방법이 없다. 
    //다만 클라이언트 세션이 넘어와도 사용하지 않고 서버 세션을 사용하는 방법 밖에 딱히 없음
    
    if(isSession ==true){
        if(session!=null){
            refDs["SESSION"]=[{
                        USER_UID: session.user.user_uid,
                        NICK_NM: session.user.nick_nm,
                        EMAIL: session.user.email,            
                    }]
        } else if(session==null && body["SESSION"]) {
            //getServerSideProps 서버로 호출되서 세션이 없는건지
            //client 브라우저에서 서버로 호출되어서 세션이 없는건지 어떻게 알수있을까?
            refDs["SESSION"]=body["SESSION"]
        } else {
            refDs["SESSION"]=[]
        }
    }
    

    let papram_body:any = {
         "_id":uuid
        ,"actID" : br
        ,"inDTName" :brRq
        ,"outDTName" :body.brRs
        ,"refDS": refDs
        
    }
    papram_body = JSON.stringify(papram_body)
    //console.log("br",br)
    //console.log("body",body)
    //console.log("papram_body",papram_body)
    return papram_body
}