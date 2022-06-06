import {Box,Stack,Button,Typography,Breadcrumbs,Link} from '@mui/material'
import type { NextPage } from 'next'
import NavigateNextIcon from '@mui/icons-material/NavigateNext'
import AccountBoxIcon from '@mui/icons-material/AccountBox';
import { FormProvider, useForm } from "react-hook-form";
import { NickNmTextFiled } from "@/form-components/NickNmTextFiled";
import { UserNmTextFiled } from "@/form-components/UserNmTextFiled";
import { EmailTextFiled } from "@/form-components/EmailTextFiled";
import { PwdTextFiled } from "@/form-components/PwdTextFiled";
import  {FormSelect}  from "@/form-components/FormSelect";
import { GenderRadio } from "@/form-components/GenderRadio";


import send from '@/utils/send';
import React, { useRef } from "react";
import { useSession, signIn } from "next-auth/react";
import { useRouter } from "next/router";

const range = (from: number, to : number) : number[] => {
	return from < to ? [from, ...range(from + 1, to)]: []
}

interface IFormInput {
    NICK_NAME: string;
    USER_NM: string;    
    EMAIL: string;
    PWD: string;
    REPWD: string;
    BIRTH: string;
    GNDR: string;
}

const defaultValues = {
    NICK_NM: "",
    USER_NM: "",
    EMAIL: "",
    PWD: "",
    REPWD: "",
    BIRTH: "",
    GNDR:"M"
};

const Join: NextPage = () => {
    const router = useRouter();
  const password = useRef({});

  const { data: session,status } = useSession()
  if(status ==='authenticated'){ //인증이 되었다면 로그인페이지로 접근하면 안됨
    router.replace("/");
  }

  
  const currentYear: number=new Date().getFullYear();
  let options:any = new Array();

  for(let i:number=currentYear-20;i>currentYear-60;i--){
      options.push({value:i.toString(),text:i.toString()})
  }
  

    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
  const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods;
  password.current = watch("PWD", "");

  const onSubmit = async (data: IFormInput) => {
        //console.log(data)   
        let param = [data]
        //const result:any= await send("BR_CM_USER_createUser", {"brRq":"IN_PARAM", "brRs":"OUT_RESULT","IN_PARAM": param })
        const result = send("BR_CM_USER_createUser", {"brRq":"IN_PARAM", "brRs":"","IN_PARAM": param })
              result.then(
                  result=>{
                      console.log({result})
                      router.replace("/member/login");
                  },
                  err=>{
                    console.log({err})
                    //에러가 나면  에러메시지를 보이고 싶다.
                    if(err.code=="USER_ERR_001"){
                        setError('EMAIL', {
                            type: "server",
                            message: err.err_msg,
                          });
                    } else {
                        setError('EMAIL', {
                            type: "server",
                            message: "에러가 발생했어요. 관리자에게 문의하세요",
                          });
                    }
                    
                  }
        )
  };
  console.log(errors);
  return (
    <>
      <Box>
            <Box m={2}>
                <Breadcrumbs aria-label="breadcrumb" 
                    separator={<NavigateNextIcon fontSize="small" />}
                    maxItems={2}
                    itemsBeforeCollapse={2}>
                    <Link underline='hover' href="#">Home</Link>
                    <Typography color='text.primary'>회원가입</Typography>
                </Breadcrumbs>
            </Box>

            <Box m={2}  sx={{width: 500,height: 300,border: 0 ,mx:"auto",justifyContent: 'center'}}>
                    <Typography variant="h5" mb={2} mr={2}><AccountBoxIcon sx={{  verticalAlign: 'middle',mr: 1,mb:1 }} />회원가입</Typography>
                    <Stack spacing={2} direction="column">
                        <NickNmTextFiled  name="NICK_NM" control={control} label="별명" rules={{ required: '별명을 입력해주세요.' }} />
                        <UserNmTextFiled  name="USER_NM" control={control} label="성명" rules={{ required: '성명을 입력해주세요.' }} />
                        <FormSelect name="BIRTH" control={control} label="생년" rules={{ required: true, }} options={options}  />
                        <GenderRadio  name="GNDR" control={control} label="성별" rules={{ required: true, }}  />
                        <EmailTextFiled name="EMAIL"  control={control} label="Email"  rules={{required:  `Email required`, pattern: { value:/^\S+@\S+$/i,  message: "Email 입력형식에 맞지 않음"  }  }} /> 
                        <PwdTextFiled  name="PWD"  control={control}  label="비밀번호"  rules={{ required: `비밀번호 required`,  minLength:{  value:3, message:"비밀번호는 3자 이상이여야 합니다" }  }} /> 
                        <PwdTextFiled name="REPWD" control={control} label="비밀번호재입력"  rules={{ required: `비밀번호재입력 required`    , validate: (value: {}) => value === password.current || "The passwords do not match"  }} />
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

export default Join