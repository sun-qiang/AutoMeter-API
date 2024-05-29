#!/bin/sh
 function getIpAddr() 
 {
        # 获取IP命令
        echo "开始获取ip"
        ipaddr=`ifconfig -a|grep inet|grep -v 127.0.0.1|grep -v inet6|awk '{print $2}'|tr -d "addr:"`
        echo "获取本机ip：$ipaddr"
        array=(`echo $ipaddr | tr '\n' ' '` )  # IP地址分割，区分是否多网卡
        num=${#array[@]}
        #echo "ip数组长度为$num"
        # 选择安装的IP地址
        if [ $num -eq 1 ]; then
                echo "*单网卡"
                local_ip=${array[*]}
        elif [ $num -gt 1 ];then
                echo -e "\033[035m******************************\033[0m"
                echo -e "\033[036m*    请选择安装的IP地址               \033[0m"
                for ((i=0; i<=$num-1;i++))
                do
                        #echo $num
                        echo -e "\033[032m*     $i : ${array[$i]}                \033[0m"
                done    
                #echo -e "\033[032m*      1 : ${array[0]}               \033[0m"
                #echo -e "\033[034m*      2 : ${array[1]}               \033[0m"
                echo -e "\033[035m******************************\033[0m"
                #选择需要安装的服务类型
                input=""
                while :
                do
                        read -r -p "*请选择安装的IP地址(序号): " input
                        case $input in
                           [0-9]*)
                                        local_ip=${array[$input]}
                                        #echo "选择网段1的IP为：${local_ip}"
                                        break
                                        ;;
                                *)
                                echo "*请输入有效的数字:"
                                        ;;
                        esac
                done
        else
                echo -e "\033[31m*未设置网卡IP，请检查服务器环境！ \033[0m"
                exit 1
        fi
} 

# 校验IP地址合法性
function isValidIp() 
{
        local ip=$1
        local ret=1
 
        if [[ $ip =~ ^[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}$ ]]; then
                ip=(${ip//\./ }) # 按.分割，转成数组，方便下面的判断
                [[ ${ip[0]} -le 255 && ${ip[1]} -le 255 && ${ip[2]} -le 255 && ${ip[3]} -le 255 ]]
                ret=$?
        fi
        return $ret
}
 
local_ip=''
echo "AutoMeter开始执行系统配置。。。。。。。。。。。。。"
getIpAddr       #自动获取IP
#echo "结束getIpAddr"
isValidIp ${local_ip}   # IP校验
if [ $? -ne 0 ]; then
        echo -e "\033[31m*自动获取的IP地址无效，请重试！ \033[0m"
        exit 1
fi
echo "*选择安装的IP地址为：${local_ip}"

os=`uname  -os`
b="Darwin"
c="centos"
d="ubuntu"

if [[ $os =~ $b ]];then
    echo "mac"
    sed -i "" "s@127.0.0.1@${local_ip}@" ../AutoMeter/conditionservice/config/application.yml
	sed -i "" "s@127.0.0.1@${local_ip}@" ../AutoMeter/dispatchservice/config/application.yml 
    sed -i "" "s@127.0.0.1@${local_ip}@" ../AutoMeter/mockservice/config/application.yml 
	sed -i "" "s@127.0.0.1@${local_ip}@" ../AutoMeter/slaverservice/config/application.yml 
	sed -i "" "s@127.0.0.1@${local_ip}@" ../AutoMeter/testcenterservice/config/application.yml
		sed -i "" "s@127.0.0.1@${local_ip}@" ../AutoMeter/testcenterapp/dist/static/config.js

else
    echo $os
    sed -i  "s@127.0.0.1@${local_ip}@" ../AutoMeter/conditionservice/config/application.yml
	sed -i  "s@127.0.0.1@${local_ip}@" ../AutoMeter/dispatchservice/config/application.yml 
    sed -i  "s@127.0.0.1@${local_ip}@" ../AutoMeter/mockservice/config/application.yml 
	sed -i  "s@127.0.0.1@${local_ip}@" ../AutoMeter/slaverservice/config/application.yml 
	sed -i  "s@127.0.0.1@${local_ip}@" ../AutoMeter/testcenterservice/config/application.yml
		sed -i  "s@127.0.0.1@${local_ip}@" ../AutoMeter/testcenterapp/dist/static/config.js

fi
sed -i 's/\r$//' ../AutoMeter/slaverservice/apache-jmeter-5.3/bin/jmeter
#echo "修改IP成功"
echo "AutoMeter系统配置成功，如果部署在云服务器以公网访问，请务必执行如下操作，将Release/v1.0/AutoMeter/testcenterapp/dist/static/config.js中的ip替换为服务器的公网访问ip，然后执行AutoMeterDockSetup-step2.sh"