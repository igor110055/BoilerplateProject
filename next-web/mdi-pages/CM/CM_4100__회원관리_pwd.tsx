import {Box,Stack,Button,Typography,Breadcrumbs,Link} from '@mui/material'
import { useForm } from "react-hook-form";
import send from '../../utils/send';
import React, { useState,useEffect,useRef,useReducer,useContext } from "react";
import {MenuContext} from "@/store/MenuStore";
import { PwdTextFiled } from "@/form-components/PwdTextFiled";

export interface IProps {
    onCallBackChgPwd:()=>void
    userUid?:number;
}

interface IFormInput {
    PWD: string;
    REPWD: string;
    
}

const defaultValues = {
    PWD: "",
    REPWD: "",
};

export  const CM_4100__회원관리_pwd = (props:IProps) => {   
    const password = useRef({});
    const [data,setData] = useState<IFormInput>(defaultValues)
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError,register,setValue } = methods;
    password.current = watch("PWD", "");

    const onSubmit = async (data: IFormInput) => {
        console.log("onSubmit");
          let param = [{
               USER_UID : props.userUid              
              ,PWD : data.PWD
              ,REPWD : data.REPWD
          }]
          const result = send("BR_CM_USER_chgPwd", {
                            "brRq":"IN_PSET"
                            ,"brRs":""
                            ,IN_PSET : param 
                        })
                result.then(
                    result=>{
                        props.onCallBackChgPwd();
                    },
                    err=>{
                      if(err.code=="USER_ERR_001"){
                          setError('PWD', {
                              type: "server",
                              message: err.err_msg,
                            });
                      }
                    }
          )
    };
    
    return (
        <>
              <Box m={2}  sx={{width: 500,height: 300,border: 0 ,mx:"auto",justifyContent: 'center'}}>
                  <form>
                      {props.userUid}
                      <Stack spacing={2} direction="column">
                        <PwdTextFiled  name="PWD"  control={control}  label="비밀번호"  rules={{ required: `비밀번호 required`,  minLength:{  value:3, message:"비밀번호는 3자 이상이여야 합니다" }  }} /> 
                        <PwdTextFiled name="REPWD" control={control} label="비밀번호재입력"  rules={{ required: `비밀번호재입력 required`    , validate: (value: {}) => value === password.current || "The passwords do not match"  }} />
                      </Stack>
  
                      <Stack spacing={2} mt={5} direction="row">
                              <Button onClick={handleSubmit(onSubmit)} variant={"outlined"}   fullWidth>비밀번호변경</Button>
                      </Stack>
                   </form>
              </Box>
        </>
    )
  }
