import NextAuth, { Profile } from 'next-auth'
import CredentialsProvider from "next-auth/providers/credentials";
import send,{send_server} from '../../../utils/send';
//https://cpro95.tistory.com/611

export default NextAuth({
    providers: [
        CredentialsProvider({
            // The name to display on the sign in form (e.g. "Sign in with...")
            name:  "유저 이메일,페스워드 방식",
            credentials: {
                email: { label: "이메일", type: "text", placeholder: "이메일입력해주세요" },
                password: { label: "패스워드", type: "password" }
            },
            async authorize(credentials:any, req) {
                const email =credentials.email;
                const pwd =credentials.password;
                
                console.log("rrrrrrrrrrrrrrr---0");
                try{
                    const data:any= await send_server("BR_CM_USER_signIn",{"brRq":"IN_PSET", "brRs":"OUT_RSET","IN_PSET":[{"EMAIL":email,"PWD": pwd}]},null)
                    console.log("rrrrrrrrrrrrrrr");
                    console.log(data)
                    const user:Profile = { user_no: data.OUT_RSET[0].USER_NO, nick_nm: data.OUT_RSET[0].NICK_NM, email: data.OUT_RSET[0].EMAIL }
                    console.log(user)
                    //return user
                    return Promise.resolve(user);

    
                } catch (err){
                    const tmp = err as any;
                    throw new Error(tmp.err_msg) 
                }
            }
        })
    ],
    secret: process.env.SECRET,
    callbacks: {
        /*
        //https://stackoverflow.com/questions/64576733/where-and-how-to-change-session-user-object-after-signing-In
        //id, name, email만 계속 리턴되서 문젠되 위에 링크에 있는 소스를 복사해서 해결
        아래 jwt하고 ,session 모두 있어야 해결됨
        */
        jwt: async ({ token, user }) => {
            user && (token.user = user)
            return token
        },
        session: async ({ session, token }) => {
            session.user = token.user as any            
            return session
        }

    }
})