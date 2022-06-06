import {Box,Stack,Button,Typography,Breadcrumbs,Link} from '@mui/material'
import type { NextPage } from 'next'
import NavigateNextIcon from '@mui/icons-material/NavigateNext'
import AccountBoxIcon from '@mui/icons-material/AccountBox';
import { useForm } from "react-hook-form";
import { GetServerSideProps,GetStaticProps,InferGetServerSidePropsType } from 'next'
import { NickNmTextFiled } from "../../form-components/NickNmTextFiled";
import { UserNmTextFiled } from "../../form-components/UserNmTextFiled";
import { EmailTextFiled } from "../../form-components/EmailTextFiled";
import { GenderRadio } from "../../form-components/GenderRadio";
import { FormSelect } from "../../form-components/FormSelect";
import send,{send_server} from '../../utils/send';
import React, { useState,useEffect } from "react";
import { useRouter } from "next/router";
import { getSession } from 'next-auth/react'

interface IFormInput {
    NICK_NM: string;
    USER_NM: string;    
    EMAIL: string;
    BIRTH: string;
    GNDR: string;
}

const defaultValues = {
    NICK_NM: "",
    USER_NM: "",
    EMAIL: "",
    BIRTH: "",
    GNDR:"M"
};

const MyPage: NextPage = ({ users }: InferGetServerSidePropsType<typeof getServerSideProps>) => {    
  const router = useRouter();
  const methods = useForm<IFormInput>({ defaultValues: defaultValues});
  const { handleSubmit, reset, control,formState: { errors } ,watch,setError,setValue,register } = methods;


  const currentYear: number=new Date().getFullYear();
  let options:any = new Array();

  for(let i:number=currentYear-20;i>currentYear-60;i--){
      options.push({value:i.toString(),text:i.toString()})
  }
  

    if(users.length > 0) {
        setValue("USER_NM",users[0].USER_NM)
        setValue("NICK_NM",users[0].NICK_NM)
        setValue("EMAIL",users[0].EMAIL)
        setValue("BIRTH",users[0].BIRTH)
        
    }


  const onSubmit = async (data: IFormInput) => {
        let param = [{
             USER_NM : data.USER_NM
            ,NICK_NM : data.NICK_NM
            ,BIRTH : data.BIRTH
            ,GNDR : data.GNDR
        }]
        //console.log({param})
        //const result:any= await send("BR_CM_USER_createUser", {"brRq":"IN_PARAM", "brRs":"OUT_RESULT","IN_PARAM": param })
        const result = send("BR_CM_USER_modifyUser", {"brRq":"IN_PSET,SESSION", "brRs":"","IN_PSET" : param })
              result.then(
                  result=>{
                      //console.log({result})
                      getMyData();
                  },
                  err=>{
                    //console.log({err})
                    //에러가 나면  에러메시지를 보이고 싶다.
                    if(err.code=="USER_ERR_001"){
                        setError('EMAIL', {
                            type: "server",
                            message: err.err_msg,
                          });
                    } else {
                        //다른 에러 표시 처리
                    }
                    
                  }
        )
  };
  //console.log(errors);

  
  const [loading,setLoading] = useState(true)
  useEffect(()=>{
      const securePage = async ()=>{
          const session = await getSession()
          if(!session) {
              router.replace("/member/login");
          } else {
              setLoading(false)
          }
      }
      securePage()
  },[])

const getMyData= async()=>{
    const data:any= await send("BR_CM_USER_retrieveBySession",{"brRq":"SESSION", "brRs":"OUT_RSET"})
    if(data.OUT_RSET.length > 0) {
        setValue("USER_NM",data.OUT_RSET[0].USER_NM)
        setValue("NICK_NM",data.OUT_RSET[0].NICK_NM)
        setValue("EMAIL",data.OUT_RSET[0].EMAIL)
        setValue("BIRTH",data.OUT_RSET[0].BIRTH)        
        setValue("GNDR",data.OUT_RSET[0].GNDR)        
    }
}       
  
  if(loading) {
      return <h2>loading.. </h2>
  }

  return (
    <>
      <Box>
            <Box m={2}>
                <Breadcrumbs aria-label="breadcrumb" 
                    separator={<NavigateNextIcon fontSize="small" />}
                    maxItems={2}
                    itemsBeforeCollapse={2}>
                    <Link underline='hover' href="#">Home</Link>
                    <Typography color='text.primary'>My Page</Typography>
                </Breadcrumbs>
            </Box>

            <Box m={2}  sx={{width: 500,height: 300,border: 0 ,mx:"auto",justifyContent: 'center'}}>
                    <Typography variant="h5" mb={2} mr={2}><AccountBoxIcon sx={{  verticalAlign: 'middle',mr: 1,mb:1 }} />My Page</Typography>
                    <Stack spacing={2} direction="column">
                        <NickNmTextFiled  name="NICK_NM" control={control} label="별명" rules={{ required: '성명을 입력해주세요.' }} readOnly={false} />
                        <UserNmTextFiled  name="USER_NM" control={control} label="성명" rules={{ required: '성명을 입력해주세요.' }} readOnly={false} />
                        <FormSelect name="BIRTH" control={control} label="생년" rules={{ required: true, }} options={options}  />
                        <GenderRadio  name="GNDR" control={control} label="성별" rules={{ required: true, }}  />
                        <EmailTextFiled name="EMAIL"  control={control} label="Email"  rules={{required:  `Email required`, pattern: { value:/^\S+@\S+$/i,  message: "Email 입력형식에 맞지 않음"  }  }}  readOnly={true}  /> 
                    </Stack>

                    <Stack spacing={2} mt={5} direction="row">
                            <Button onClick={handleSubmit(onSubmit)} variant={"contained"} fullWidth>저장</Button>
                            <Button onClick={() => reset()} variant={"outlined"} fullWidth>초기화</Button>
                    </Stack>
            </Box>
    </Box>
      </>
  );
};
export default MyPage

export const getServerSideProps: GetServerSideProps = async (context) => {
    const session = await getSession(context);
    let user_no="";
    if(session && session.user) {
        const tmp = session.user as Record<string, unknown>
        user_no=tmp.user_no as string;
    } 
    if(!session){
        return { props : {users : []}}
    }
    //세션은 서버단에서넣을꺼야
    //라고 생각했는데  getServerSideProps 자체가  서버여서
    //마치 이함수 자체가 proxy처럼 동작해서   
    //새로운 소켓이 열리는것 처럼 동작해서   세션을 얻어오질 못한다.
    //그래서 세션을 넣어줘야한다.
    const data:any= await send_server("BR_CM_USER_retrieveBySession",{"brRq":"SESSION", "brRs":"OUT_RSET"},session)
    if(!data){
        return { props : {users : []}}
    }
   
   return {
       props : {
            users : data.OUT_RSET
       }
   }
 }
