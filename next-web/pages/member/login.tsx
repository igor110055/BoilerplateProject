import {Box,
    Stack,
    Button,
    Typography,
    Breadcrumbs,
    Link
} from '@mui/material'
import type { NextPage } from 'next'
import NavigateNextIcon from '@mui/icons-material/NavigateNext'
import AccountBoxIcon from '@mui/icons-material/AccountBox';
import { FormProvider, useForm } from "react-hook-form";
import { EmailTextFiled } from "../../form-components/EmailTextFiled";
import { PwdTextFiled } from "../../form-components/PwdTextFiled";
import { useSession, signIn } from "next-auth/react";
import { useRouter } from "next/router";

type FormValues = {
    EMAIL: string;
    PWD: string;
  };

  interface IFormInput {
    EMAIL: string;
    PWD: string;
  }

const defaultValues = {
    EMAIL: "",
    PWD: ""
};
const SignIn: NextPage = () => {
  const router = useRouter();
  const methods = useForm<IFormInput>({ defaultValues: defaultValues});
  const { data: session,status } = useSession()
  //console.log({session})
  //console.log({status})
  if(status ==='authenticated'){ //인증이 되었다면 로그인페이지로 접근하면 안됨
    router.replace("/");
  }



  const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods; 
  const onSubmit = async (data: IFormInput) => {
        const result:any = await signIn("credentials", {
           redirect: false,
           email: data.EMAIL,
           password: data.PWD,
         });
         console.log("err11")
         console.log({result})
         if (!result.error) {
            router.replace("/");
          } else {
    

            setError('EMAIL', {
                type: "server",
                message: `Error Occured : ${result.error}`,
            });
          }
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
                    <Typography color='text.primary'>Sign In</Typography>
                </Breadcrumbs>
            </Box>

            <Box m={2}  sx={{
                            width: 500,
                            height: 300,
                            border: 0 ,
                            mx:"auto",
                            justifyContent: 'center'
                        }}>
                    <Typography variant="h5" mb={2} mr={2}><AccountBoxIcon sx={{  verticalAlign: 'middle',mr: 1,mb:1 }} />로그인</Typography>
                    <Stack spacing={2} direction="column">
                        <EmailTextFiled     name="EMAIL"     control={control} label="Email"          rules={{required:  `Email required`, pattern: {value:/^\S+@\S+$/i, message: "Email 입력형식에 맞지 않음" } }} />
                        <PwdTextFiled       name="PWD"       control={control} label="비밀번호"        rules={{ required: `비밀번호 required`,  minLength:{value:3,message:"비밀번호는 3자 이상이여야 합니다" }  }} />
                    </Stack>

                    <Stack spacing={2}  mt={5} direction="row">
                            <Button onClick={handleSubmit(onSubmit)} variant={"contained"} fullWidth>로그인</Button>
                            <Button onClick={() => reset()} variant={"outlined"} fullWidth>초기화</Button>
                    </Stack>
            </Box>
    </Box>
      </>
  );
};

export default SignIn