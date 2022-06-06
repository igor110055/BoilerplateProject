import Grid,{GridHandler} from '@/form-components/Grid';
import GridCombo from '@/form-components/GridCombo';
import GridTextEditor from '@/form-components/GridTextEditor';
import { useForm } from "react-hook-form";
import {FormSelect}  from '@/form-components/FormSelect';
import {FormTextFiled} from '@/form-components/FormTextFiled';
import React, { useState,useEffect,useRef,useReducer,useContext } from "react";
import send from '@/utils/send';
import {Stack,Button} from '@mui/material'
import {MenuContext} from "@/store/MenuStore";
import TuiEditor from '@/form-components/TuiEditor';
import sanitize from "sanitize-html";
import { IPageProps } from './CM_6200__게시판';

interface IFormInput {
    GRP_CD: string;
    BRD_SEQ: string;
    UPDT_DTM: string;
    CRT_DTM: string;
    TTL: string;
    TTL_TEXT: string;
    CNTNT: string;
    CNTNT_TEXT: string;

    

  }

const defaultValues = {
    GRP_CD: "",
    BRD_SEQ:"",
    UPDT_DTM:"",
    CRT_DTM:"",
    TTL:"",
    TTL_TEXT:"",
    CNTNT:"",
    CNTNT_TEXT:""
};

export  const CM_6200__게시판_new = (props:IPageProps) => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;
    const [data,setData] = useState<IFormInput>(defaultValues)
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError,register,setValue } = methods;
    const [grpSeqData,setGrpSeqData] = useState([]);

    useEffect(()=>{
        const getData = async ()=>{
            const data:any= await send("BR_CM_BOARD_GROUP_FIND", 
            {
                 brRq: 'IN_DATA'
                ,brRs: 'OUT_DATA'
                ,IN_DATA:[{}]
            });

            const tmp= data.OUT_DATA.map((m:any)=>{
                return {
                    value: m["GRP_CD"],
                    text: m["GRP_NM"]            
                };
            })
            setGrpSeqData(tmp)
        }
        getData();
    },[]);

    const saveHandler= async (data: IFormInput) => {        
        messageConfirm("저장하시겠습니까?.",function()  {
            //_this.showProgress();	
            data.TTL_TEXT = sanitize(data.TTL,{allowedTags: [""]})
            data.CNTNT_TEXT = sanitize(data.CNTNT,{allowedTags: [""]})
            console.log({data})
            send('BR_CM_BOARD_crt',{
                brRq 		: 'IN_DATA,SESSION'
                ,brRs 		: ''
                ,IN_DATA	: [data]
            }).then(function(data){
                //_this.hideProgress();
                if(data){
                    messageAlert("저장하였습니다",function()  {
                        props.setMode("list");
                    });
                }
            }).catch(function(e){
                console.log({e})
                messageAlert(e.err_msg)

            })                      
        })
    }

    const editorHandler = (value:string)=>{
        //console.log(a)
        setValue("CNTNT",value)

    }
    
    
    return (
            <>
            <div style={{
                                        border: "1px solid #ccc",
                                        padding: "10px",
                                        borderRadius: "10px"
                                      }}>
                <div className="container">
                    <div className="row">
                        <div className="col-sm">
                            <FormSelect name="GRP_SEQ" control={control} label="게시판" sx={{width:200}} rules={{ required: false }}  options={grpSeqData}   />
                        </div>
                    </div>
                    <div className="row">
                        <div className="col">
                            <div className="form-group">
                            <div className="input-group">
                                <FormTextFiled  name="TTL"  control={control}  label="제목"  rules={{ required: `제목 required` }} /> 
                                <input type="text"  {...register("TTL_TEXT")} style={{display:"none"}}  />
                            </div>
                            </div>
                        </div>
                    </div>
                    <div className="row">
                        <div className="col">
                            <TuiEditor   onChange={editorHandler} height="300px" />
                            <input type="text"  {...register("CNTNT")} style={{display:"none"}}  />
                            <input type="text"  {...register("CNTNT_TEXT")} style={{display:"none"}}  />
                        </div>
                    </div>
                </div>
                <div style={{padding: '10px'}}>
                        <Button variant="contained" color="success"  onClick={()=>{props.setMode("list");}}>리스트</Button>     
                        <Button variant="contained" color="success"  onClick={handleSubmit(saveHandler)}>저장</Button>       
                    </div>
                </div>
            </>
    )
  }
  
  

