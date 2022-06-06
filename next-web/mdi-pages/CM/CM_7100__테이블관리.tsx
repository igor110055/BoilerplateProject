import Grid,{GridHandler} from '@/form-components/Grid';
import React, { useState,useEffect,useRef,useContext } from "react";
import send from '@/utils/send';
import {Stack,Button} from '@mui/material'
import {MenuContext} from "@/store/MenuStore";
import GridTextEditor from '@/form-components/GridTextEditor';


export  const CM_7100__테이블관리 = () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;
    
    const childRef = useRef<GridHandler>(null);
    const [ctgData,setCtgData] = useState([]);

    const fnSearch= async () => {
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_CM_DB_POSTGRESQL_retrieveTableList",
                                        PARAM: {
                                            brRq : ''
                                            ,brRs : 'OUT_DATA'
                                        }
                                    });

        }
    }
    useEffect(()=>{
        fnSearch()
    },[]);

    const columns = [
        { key: 'SCHEMA_NAME', name: 'SCHEMA_NAME', width: 130 ,  sortable: true,
            summaryFormatter() {
                return <strong>Total</strong>;
            }
        },
        { key: 'TABLE_NAME', name: 'TABLE_NAME', width: 300 },
        { key: "TABLE_COMMENT", name: 'TABLE_COMMENT', width: 400 ,  sortable: true  ,editor: GridTextEditor},
    ];
    
    const saveHandler=(event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        if(childRef.current){
            const data = childRef.current.getModifiedRows();
            var updt_cnt= data.updatedRows.length;
            if((updt_cnt)==0) {
                messageAlert("저장할 내용이 존재하지 않습니다.");
                return;
            }
            messageConfirm("저장하시겠습니까?",function()  {
                //_this.showProgress();	
                send('BR_CM_DB_POSTGRESQL_saveTableCmt',{
                    brRq 		: 'UPDT_DATA'
                    ,brRs 		: ''
                    ,UPDT_DATA	: data.updatedRows
                }).then(function(data){
                    //_this.hideProgress();
                    if(data){
                        messageAlert("테이블코멘트를 저장하였습니다.",function()  {
                            fnSearch()
                        });
                    }
                })                      
            })
        }
    }

    return (
            <>
            <Stack
                mt={2}
                direction="row"
                justifyContent="flex-start"
                alignItems="flex-start"
                spacing={0.5}
                >
                    <Button variant="contained" color="success"   onClick={fnSearch}>조회</Button>
                    <Button variant="contained" color="success"  onClick={saveHandler}>저장</Button>                                                                        
            </Stack>
            <Grid style={{ height: 600, width: '100%' }} columns={columns}  checkbox={true} showRowStatus={true} ref={childRef} />
            </>
    )
  }

