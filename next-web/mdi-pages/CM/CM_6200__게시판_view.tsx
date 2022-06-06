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
import { IPageProps } from './CM_6200__게시판';

interface IFormInput {
    GRP_SEQ: string;
    BRD_SEQ: string;
    UPDT_DTM: string;
    CRT_DTM: string;
    TTL: string;
    CNTNT: string;

    

  }

const defaultValues = {
    GRP_SEQ: "",
    BRD_SEQ:"",
    UPDT_DTM:"",
    CRT_DTM:"",
    TTL:"",
    CNTNT:"",
};

export  const CM_6200__게시판_view = (props:IPageProps) => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;
    const [data,setData] = useState<IFormInput>(defaultValues)
    useEffect(()=>{
        const getData = async ()=>{
            const data:any= await send("BR_CM_BOARD_FIND_BY_BRD_SEQ", {brRq: 'IN_DATA'
            ,brRs: 'OUT_DATA'
            ,IN_DATA: [{
                BRD_SEQ : props.brdSeq
            }]
            });

            if(data.OUT_DATA.length>0){
                setData(data.OUT_DATA[0]);
            }
        }
        getData();
    },[]);

    const delHandler= async () => {
        messageConfirm("삭제하시겠습니까?.",function()  {
            send('BR_CM_BOARD_rm',{
                brRq 		: 'IN_DATA,SESSION'
                ,brRs 		: ''
                ,IN_DATA	: [{BRD_SEQ:data.BRD_SEQ}]
            }).then(function(data){
                //_this.hideProgress();
                if(data){
                    messageAlert("삭제하였습니다.",function()  {
                        props.setMode("list");
                    });
                }
            }).catch(function(e){
                console.log({e})
                messageAlert(e.err_msg)

            })                      
        })
    }
    const modifyHandler= async()=>{
            props.setBrdSeq!(data.BRD_SEQ);
            props.setMode("edit");
    }

    return (
            <>
            <div style={{
                                        border: "1px solid #ccc",
                                        padding: "10px",
                                        borderRadius: "10px"
                                      }}>
                <div className="container">
                    <div className="row row-cols-4">
                    <div className="col-sm">
                        <div className="form-group">
                        <div className="input-group">
                            <span className="input-group-addon">게시판선택</span>
                            <div>{data.GRP_SEQ}</div>
                        </div>
                        </div>
                    </div>
                    <div className="col">
                        <div className="form-group">
                        <div className="input-group">
                            <span className="input-group-addon">BRD_SEQ</span>
                            <div className="form-control">{data.BRD_SEQ}</div>
                        </div>
                        </div>
                    </div>
                    <div className="col">
                        <div className="form-group">
                        <div className="input-group">
                            <span className="input-group-addon">수정일시</span>
                            <div className="form-control">{data.UPDT_DTM}</div>
                        </div>
                        </div>
                    </div>
                    <div className="col">
                        <div className="form-group">
                        <div className="input-group">
                            <span className="input-group-addon">생성일시</span>
                            <div className="form-control">{data.CRT_DTM}</div>
                        </div>
                        </div>
                    </div>
                    </div>
                    <div className="row">
                    <div className="col">
                        <div className="form-group">
                        <div className="input-group">
                            <span className="input-group-addon">제목</span>
                            <div className="form-control">{data.TTL}</div>
                        </div>
                        </div>
                    </div>
                    </div>
                    <div className="row">
                    <div className="col">
                        <div className="form-control"
                            dangerouslySetInnerHTML={{
                                __html: data.CNTNT
                            }}>
                        </div>
                    </div>
                    </div>
                </div>
                <div style={{padding: '10px'}}>
                    <Button variant="contained" color="success" onClick={() => {props.setMode("list");}}>리스트</Button>                  
                    <Button variant="contained" color="success" onClick={modifyHandler}>수정</Button>      
                    <Button variant="contained" color="success" onClick={delHandler}>삭제</Button>                                                                              
                    </div>
                </div>
            </>
    )
  }
  
  

