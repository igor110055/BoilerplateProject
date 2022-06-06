import { Button, Dialog, DialogActions, DialogContent, DialogTitle } from "@mui/material";
import {forwardRef,useState,useEffect,useRef,useImperativeHandle} from "react";

export type ConfirmDialogHandler = {
    setValue:(value:string, fn:any)=>void;
};
  
const  ConfirmDialog = forwardRef<ConfirmDialogHandler,any>((props,ref)=> {
    const [open, setOpen]=useState(false)
    const [value, setValue] = useState("");
    const localFn = useRef<Function>();
    //console.log({localFn})

    useImperativeHandle(ref, () => ({
        setValue(value:string,fn:any) {
            //console.log("ConfirmDialog");
            setOpen(true);
            setValue(value);
            //setFn(fn)
            localFn.current=fn;
            //console.log({localFn})
        }
      }))
 
    const handleCancel = () => {
        setOpen(false);
    };
  
    const handleOk = () => {
        if(localFn.current){
            localFn.current();
        }
        
        setOpen(false);
    };
  
    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
      setValue((event.target as HTMLInputElement).value);
    };
  
    return (
      <Dialog
        sx={{ '& .MuiDialog-paper': { width: '80%', maxHeight: 435 } }}
        maxWidth="xs"
        open={open}
      >
        <DialogTitle>Confirm</DialogTitle>
        <DialogContent dividers>{value}
         
        </DialogContent>
        <DialogActions>
          <Button autoFocus onClick={handleCancel}>
            Cancel
          </Button>
          <Button onClick={handleOk}>Ok</Button>
        </DialogActions>
      </Dialog>
    );
  });
  
  export default ConfirmDialog;