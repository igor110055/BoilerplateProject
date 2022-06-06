import type { NextPage } from 'next'
import { GetServerSideProps,InferGetServerSidePropsType } from 'next'
import {useRouter} from 'next/router'
import send,{send_server} from '../../../utils/send';

const EmailCert: NextPage = ({ NICK_NM }: InferGetServerSidePropsType<typeof getServerSideProps>) => {    
    return <h1>({NICK_NM})님 Email 인증 되었습니다.  </h1>
}

export default EmailCert


export const getServerSideProps: GetServerSideProps = async ({query}) => {
    const email_cert_uuid =query.email_cert_uuid
    //console.log({email_cert_uuid})
    const data:any= await send_server("BR_CM_USER_runCertEmail",{"brRq":"IN_PSET", "brRs":"OUT_RSET","IN_PSET":[{"EMAIL_CERT_UUID":email_cert_uuid}] },null)
    //console.log(data);
   
   return {
       props : {
           NICK_NM : data.OUT_RSET[0].NICK_NM         
       }
   }
 }
