import Grid,{GridHandler} from '@/form-components/Grid';
import GridCombo from '@/form-components/GridCombo';
import GridTextEditor from '@/form-components/GridTextEditor';
import React, { useEffect,useRef,useContext } from "react";
import send from '@/utils/send';
import {Stack,Button} from '@mui/material'
import {MenuContext} from "@/store/MenuStore";

export const CM_1200__테이블도메인관리 = () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;

    const childRef = useRef<GridHandler>(null);
    const fnSearch= async () => {
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_CM_DOMAIN_find",
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
        { key: 'DMN_NO', name: 'SEQ', width: 80 ,  sortable: true, dataType:"number"  ,
            summaryFormatter() {
                return <strong>Total</strong>;
            }
        },
        { key: 'DMN_CD', name: '도메인코드', width: 100 ,editor: GridTextEditor},
        { key: "DMN_NM", name: '도메인명', width: 200 ,  sortable: true  ,editor: GridTextEditor},
        { key: 'DATA_TYPE', name: '크기', width: 100 ,editor: GridTextEditor},            
        { key: 'RMK', name: '비고', width: 200 ,editor: GridTextEditor},            
        { key: 'CRT_DTM', name: '생성일', width: 160 },
        { key: 'UPDT_DTM', name: '수정일', width: 160 },
    ];
    
    const saveHandler=(event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        if(childRef.current){
            const data = childRef.current.getModifiedRows();
            var crt_cnt	= data.createdRows.length;
            var updt_cnt= data.updatedRows.length;
            if((crt_cnt+updt_cnt)==0) {
                messageAlert("저장할 내용이 존재하지 않습니다.");
                return;
            }
            messageConfirm("저장하시겠습니까?",function()  {
                //_this.showProgress();	
                send('BR_CM_DOMAIN_SAVE',{
                    brRq 		: 'IN_DATA,UPDT_DATA,SESSION'
                    ,brRs 		: ''
                    ,IN_DATA	: data.createdRows
                    ,UPDT_DATA	: data.updatedRows
                }).then(function(data){
                    //_this.hideProgress();
                    if(data){
                        messageAlert("저장되었습니다",function()  {
                            fnSearch()
                        });
                    }
                })                      
            })
        }
    }

    const delHandler=(event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        if(childRef.current){
            const data = childRef.current.getCheckedData();
            console.log({data});
            if(data.length<=0) {
                messageAlert('선택된 항목이 없습니다.');
                return;
            }
            messageConfirm("삭제하시겠습니까?",function()  {
                //_this.showProgress();	
                send('BR_CM_DOMAIN_RM',{
                    brRq 		: 'IN_DATA,SESSION'
                    ,brRs 		: ''
                    ,IN_DATA	: data
                }).then(function(data){
                    //_this.hideProgress();
                    if(data){
                        messageAlert("삭제되었습니다",function()  {
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
                    <Button variant="contained" color="success" onClick={() => {
                                                                            if(childRef.current){
                                                                                const tmp = childRef.current.addRow();
                                                                                console.log({tmp})
                                                                            }}
                                                                        }>추가</Button>
                    <Button variant="contained" color="success"  onClick={saveHandler}>저장</Button>                                                                        
                    <Button variant="contained" color="success"  onClick={delHandler}>삭제</Button>                                                                        
            </Stack>
            <Grid style={{ height: 600, width: '100%' }} columns={columns}  checkbox={true} showRowStatus={true} ref={childRef} />
            </>
    )
  }
