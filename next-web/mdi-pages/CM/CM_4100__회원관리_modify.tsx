import {Box,Stack,Button,Typography,Breadcrumbs,Link} from '@mui/material'
import { useForm } from "react-hook-form";
import { NickNmTextFiled } from "../../form-components/NickNmTextFiled";
import { UserNmTextFiled } from "../../form-components/UserNmTextFiled";
import { EmailTextFiled } from "../../form-components/EmailTextFiled";
import { GenderRadio } from "../../form-components/GenderRadio";
import { FormSelect } from "../../form-components/FormSelect";
import send from '../../utils/send';
import React, { useState,useEffect,useRef,useReducer,useContext } from "react";
import {MenuContext} from "@/store/MenuStore";
import { rest } from 'lodash';
import { FormTextFiled } from '@/form-components/FormTextFiled';
import { CM_4100__회원관리_pwd } from './CM_4100__회원관리_pwd';

export interface IProps {
    fnSearch: () => Promise<void>;
    setMode?: React.Dispatch<React.SetStateAction<string>>;
    userNo?:number;
}


interface IFormInput {
    USER_ID: string;
    NICK_NM: string;
    USER_NO: string;    
    USER_NM: string;    
    EMAIL: string;
    BIRTH: string;
    USE_YN: string;
    GNDR: string;
}

const defaultValues = {
    USER_ID: "",
    NICK_NM: "",
    USER_NO: "",
    USER_NM: "",
    EMAIL: "",
    BIRTH: "",
    USE_YN: "",
    GNDR:""
};

const PasswordChange = () =>{

    return (<></>);

}

export  const CM_4100__회원관리_modify = (props:IProps) => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm,inlineDialog,inlineDialogClose } = menuStoreContext;
    const [loading,setLoading] = useState(false)
    const [useYn,setUseYn] = useState([])
    const [data,setData] = useState<IFormInput>(defaultValues)
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError,register,setValue } = methods;

    const currentYear: number=new Date().getFullYear();
    let options:any = new Array();
    options.push({value:"",text:"-선택-"});
    for(let i:number=currentYear-20;i>currentYear-60;i--){
        options.push({value:i.toString(),text:i.toString()})
    }
    const getData= async()=>{
        if(props.userNo!=0){
            const data:any= await send("BR_CM_USER_retrieveByUserNo",{
                "brRq":"IN_PSET,SESSION"
                , "brRs":"OUT_RSET"
                ,IN_PSET:[{USER_NO:props.userNo}]
            })
            if(data.OUT_RSET.length > 0) {
                reset(data.OUT_RSET[0])
            } 
        } else {
            reset(defaultValues);
        }
        setLoading(true)
    } 

    useEffect(()=>{
            const getInitData = async ()=>{
                const data:any= await send("BR_CM_CD_FIND", {brRq: 'IN_DATA'
                ,brRs: 'OUT_DATA'
                ,IN_DATA: [  { GRP_CD : 'USE_YN',  USE_YN: 'Y'}]
                });

                const tmp= data.OUT_DATA.map((m:any)=>{
                    return {
                        value: m["CD"],
                        text: m["CD_NM"]            
                    };
                })
                setUseYn(tmp)
            }
            getInitData();
        
            getData();
    },[props])
  
    const onSubmit = async (data: IFormInput) => {
        console.log("onSubmit");
          let param = [{
               USER_NM : data.USER_NM
              ,USER_NO : data.USER_NO              
              ,NICK_NM : data.NICK_NM
              ,BIRTH : data.BIRTH
              ,GNDR : data.GNDR
          }]
          const result = send("BR_CM_USER_modifyByUserNo", {"brRq":"IN_PSET,SESSION", "brRs":"","IN_PSET" : param })
                result.then(
                    result=>{
                        getData();
                        //상단 회원관리도 재조회 해야한다.
                        props.fnSearch();
                    },
                    err=>{
                      if(err.code=="USER_ERR_001"){
                          setError('EMAIL', {
                              type: "server",
                              message: err.err_msg,
                            });
                      }
                    }
          )
    };
    
    if(loading==false) {
        return <h2>loading.. </h2>
    }
    
    const onCallBackChgPwd = ()=>{
        console.log("onCallBackChgPwd")
        inlineDialogClose();
        props.fnSearch();
    }
    
    return (
        <>
              <Box m={2}  sx={{width: 500,height: 300,border: 0 ,mx:"auto",justifyContent: 'center'}}>
                  //{props.userNo}//
                      <Stack spacing={2} direction="column">
                          <FormTextFiled  name="USER_ID" control={control} label="아이디" InputProps = {{readOnly: true}} />
                          <NickNmTextFiled  name="NICK_NM" control={control} label="별명" rules={{ required: '별명을 입력해주세요.' }} readOnly={false} />
                          <UserNmTextFiled  name="USER_NM" control={control} label="성명" rules={{ required: '성명을 입력해주세요.' }} readOnly={false} />
                          <FormSelect name="BIRTH" control={control} label="생년" rules={{ required: true, }} options={options}  />
                          <GenderRadio  name="GNDR" control={control} label="성별" rules={{ required: true, }}  />
                          <EmailTextFiled name="EMAIL"  control={control} label="Email"  rules={{required:  `Email required`, pattern: { value:/^\S+@\S+$/i,  message: "Email 입력형식에 맞지 않음"  }  }}  readOnly={true}  /> 
                          <FormSelect name="USE_YN" control={control} label="사용여부" rules={{ required: true, }} options={useYn}  />
                      </Stack>
  
                      <Stack spacing={2} mt={5} direction="row">
                              <Button onClick={handleSubmit(onSubmit)} variant={"contained"} fullWidth>저장</Button>
                              <Button onClick={() => reset()} variant={"outlined"} fullWidth>초기화</Button>
                              <Button onClick={() => {
                        
                                  inlineDialog(<CM_4100__회원관리_pwd userNo={props.userNo}   onCallBackChgPwd={onCallBackChgPwd} />,undefined,false,"sm"); 
                              }
                            } variant={"outlined"} fullWidth>비밀번호변경</Button>
                      </Stack>
              </Box>
        </>
    )
  }
