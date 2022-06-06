import React, { useState,useEffect,useRef, useContext } from "react";
import send from '@/utils/send';
import {MenuContext} from "@/store/MenuStore";
import Button from "@mui/material/Button";

interface IFormInput {
}

const defaultValues = {
};


export const BINANCE_0135__빠른인출스위치= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm,inlineDialog,inlineDialogClose } = menuStoreContext;

    const fnDisableFastWithdrawSwitchHandler=(event: React.MouseEvent<HTMLButtonElement>) => {

        messageConfirm("빠른인출스위치비활성화하시겠습니까?",function()  {
            //_this.showProgress();	
            send('BR_BINANCE_WALLET_POST_SAPI_V1_DisableFastWithdrawSwitch',{
                brRq 		: ''
                ,brRs 		: ''
            }).then(function(data){
                //_this.hideProgress();
                if(data){
                    messageAlert("빠른인출스위치비활성화되었습니다",function()  {
                        
                    });
                }
            })                      
        })
    }

    const fnEnableFastWithdrawSwitchHandler=(event: React.MouseEvent<HTMLButtonElement>) => {

        messageConfirm("빠른인출스위치활성화하시겠습니까?",function()  {
            //_this.showProgress();	
            send('BR_BINANCE_WALLET_POST_SAPI_V1_EnableFastWithdrawSwitch',{
                brRq 		: ''
                ,brRs 		: ''
            }).then(function(data){
                //_this.hideProgress();
                if(data){
                    messageAlert("빠른인출스위치활성화되었습니다",function()  {
                        
                    });
                }
            })                      
        })
    }


    return (
            <>
                <pre className="tal lh12">
                <h3>Enable Fast Withdraw Switch (USER_DATA)</h3>
                POST /sapi/v1/account/enableFastWithdrawSwitch (HMAC SHA256)
                <b>Weight(IP):</b>  1
                </pre>
                <hr />
                <pre className="tal lh12">
                <ul>
                <li>This request will enable fastwithdraw switch under your account.
                    You need to enable "trade" option for the api key which requests this endpoint.</li>
                <li>When Fast Withdraw Switch is on, transferring funds to a Binance account will be done instantly. There is no on-chain transaction, no transaction ID and no withdrawal fee.</li>
                </ul>
                </pre>

                <hr />

                <pre className="tal lh12">
                <h3>Disable Fast Withdraw Switch (USER_DATA)</h3>
                POST /sapi/v1/account/disableFastWithdrawSwitch (HMAC SHA256)
                <b>Weight(IP):</b>  1
                </pre>
                <hr />
                <pre className="tal lh12">
                <b>Caution:</b>
                <ul>
                    <li>This request will disable fastwithdraw switch under your account.</li>
                    <li>You need to enable "trade" option for the api key which requests this endpoint.</li>
                </ul>
                </pre>
                <Button variant="contained" color="success"  onClick={fnDisableFastWithdrawSwitchHandler}>빠른인출스위치비활성화</Button>
                <Button variant="contained" color="success"  onClick={fnEnableFastWithdrawSwitchHandler}>빠른인출스위치활성화</Button>
            </>
    )
  }
