export default class SocketService{

    static instance = null
    static get Instance(){
        if(!this.instance){
            this.instance = new SocketService()
        }
        return this.instance
    }

    ws = null

    //存储回调函数
    callBackMapping = {}

    //标识是否连接成功
    connected = false

    //记录重试的次数
    sendRetryCount = 0
    connectRetryCount = 0

    //定义连接服务器的方法
    connect(){
        if(!window.WebSocket){
            return console.log("您的浏览器不支持websocket")
        }
        
        const socketUrl = `ws://127.0.0.1:3000/websocket/test/`;
        this.ws = new WebSocket(socketUrl)

        //连接成功
        this.ws.onopen = () =>{
            console.log("连接服务器成功")
            this.connected = true
            //重置重新连接的次数
            this.connectRetryCount = 0
        }

        //连接服务器失败
        this.ws.onclose = () =>{
            console.log("disconnected")
            this.connected = false
            this.connectRetryCount++
            setTimeout(()=>{
                this.connect()
            }, this.connectRetryCoun * 500)
        }

        //接收数据
        this.ws.onmessage = msg =>{

            console.log("msg: ", msg)
            const chartData = JSON.parse(msg.data)

            if(this.callBackMapping["trendData"]){
                this.callBackMapping["trendData"].call(this, chartData)
            }
            if(this.callBackMapping["trendDataAge"]){
                this.callBackMapping["trendDataAge"].call(this, chartData)
            }
            if(this.callBackMapping["trendDataSex"]){
                this.callBackMapping["trendDataSex"].call(this, chartData)
            }
            if(this.callBackMapping["trendDataProvince"]){
                this.callBackMapping["trendDataProvince"].call(this, chartData)
            }
            if(this.callBackMapping["trendDataNews"]){
                this.callBackMapping["trendDataNews"].call(this, chartData)
            }
            if(this.callBackMapping["trendDataWorld"]){
                this.callBackMapping["trendDataWorld"].call(this, chartData)
            }
        }

    }

    //回调函数的注册
    registerCallBack(socketType, callBack){
        this.callBackMapping[socketType] = callBack
    }

    //取消某一个回调函数
    unRegisterCallBack(socketType){
        this.callBackMapping[socketType] = null
    }

    //发送数据
    send(data){
        //判断此时此刻有没有连接成功
        if(this.connected){
            this.sendRetryCount = 0
            this.ws.send(JSON.stringify(data))
        }else{
            this.sendRetryCount++
            setTimeout(()=>{
                this.send(data)
            }, this.sendRetryCount * 500)
        }

        this.ws.send(JSON.stringify(data))
    }


}