package com.cjack.meetingroomadmin.util;

import org.springframework.util.StringUtils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by root on 5/15/19.
 */
public class CustomerStringUtil {

    /**
     * 拼接字符串，用[joinStrs]替换字符串里面的{n}占位符里面的内容，n从0开始
     * @param str
     * @param joinStrs
     * @return
     */
    public static String handleStrJoin( String str, String [] joinStrs){
        if( str == null || str.equals( "")){
            return str;
        }
        if( joinStrs == null || joinStrs.length == 0){
            return str;
        }
        for( int i =0; i < joinStrs.length ; i++){
            //这里可能会有性能问题
            str = str.replace( "{" + i + "}", joinStrs[i]);
        }
        return str;
    }

    public static String toLikeStr( String str){
        return "%" + str + "%";
    }

    public static String randomNumberStr( int len){
        StringBuffer buffer = new StringBuffer( "0123456789");
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        int range = buffer.length();
        for ( int i = 0; i < len; i ++){
            sb.append( buffer.charAt( r.nextInt( range)));
        }
        return sb.toString();
    }

    public static String randomStrOnlyUpperChar( int len){
        StringBuffer buffer = new StringBuffer( "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        int range = buffer.length();
        for ( int i = 0; i < len; i ++){
            sb.append( buffer.charAt( r.nextInt( range)));
        }
        return sb.toString();
    }

    /**
     * 比较两个字符串是否相等
     * @param str1
     * @param str2
     * @return
     */
    public static boolean compareStr( String str1, String str2){
        if( str1 == null){
            if( str2 == null){
                return true;
            }
            return false;
        }
        return str1.equals( str2);
    }

    public static String replaceWidth( String content){
        StringBuffer sb = new StringBuffer();
        if(StringUtils.isEmpty( content)){
            return null;
        }
        Pattern p = Pattern.compile("(width:.+?;)") ;
        Matcher m = p.matcher( content) ;
        while( m.find() ){
            String tmp = m.group() ;
            String v = "width: 100%;";
            //注意，在替换字符串中使用反斜线 (\) 和美元符号 ($) 可能导致与作为字面值替换字符串时所产生的结果不同。
            //美元符号可视为到如上所述已捕获子序列的引用，反斜线可用于转义替换字符串中的字面值字符。
            v = v.replace("\\", "\\\\").replace("$", "\\$");
            //替换掉查找到的字符串
            m.appendReplacement(sb, v) ;
        }
        //别忘了加上最后一点
        m.appendTail(sb);
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println( replaceWidth( "<div id=\"xwzx_content_all\" style=\"padding-top: 30px; padding-bottom: 20px; margin-right: auto; margin-left: auto; font-size: medium; line-height: 36px; width: 1000px; border-bottom: 1px solid rgb(230, 230, 230); color: rgb(51, 51, 51); font-family: &quot;Microsoft YaHei&quot;;\"><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\">为贯彻落实国务院激励企业加大研发投入、优化研发费用加计扣除政策实施的举措，深入开展“我为纳税人缴费人办实事暨便民办税春风行动”，让纳税人充分享受政策红利，激励企业增加研发投入积极性，方便企业提前享受研发费用加计扣除优惠政策，国家税务总局制发《关于进一步落实研发费用加计扣除政策有关问题的公告》（2021年第28号，以下简称《公告》）。现解读如下：</p><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\"><span style=\"font-weight: bold;\">一、《公告》出台的主要背景是什么？</span></p><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\">答：研发费用加计扣除政策是支持企业科技创新的有效政策抓手。一直以来，党中央、国务院高度重视研发费用加计扣除政策的贯彻落实，多措并举，让企业充分享受政策红利。近期，国务院又推出了进一步激励企业加大研发投入、优化研发费用加计扣除政策实施的举措。为把国务院的决策部署落实落细，增加企业获得感，减轻办税负担，我局制发《公告》。</p><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\"><span style=\"font-weight: bold;\">二、《公告》主要包括哪些内容？</span></p><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\">答：《公告》主要包括三项内容：</p><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\">一是在今年10月份预缴申报时，允许企业自主选择提前享受前三季度研发费用加计扣除优惠。此前，研发费用加计扣除优惠在年度汇算清缴时享受，平时预缴时不享受。今年3月底，财税部门明确，在10月份预缴申报时，允许企业享受上半年研发费用加计扣除优惠。根据国务院最新部署，《公告》明确在今年10月份预缴申报时，允许企业多享受一个季度的研发费用加计扣除。</p><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\">二是增设优化简化研发费用辅助账样式。为便于企业准备合规的研发费用辅助账，税务总局2015年制发《关于企业研究开发费用税前加计扣除政策有关问题的公告》（2015年第97号，以下简称97号公告），发布了2015版研发支出辅助账样式，对帮助纳税人准确归集研发费用和享受优惠政策起到积极作用。考虑到部分中小微企业财务核算水平不高，准确归集、填写2015版研发支出辅助账有一定难度，《公告》增设了2021版研发支出辅助账样式，降低了填写难度。</p><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\">三是调整优化了“其他相关费用”限额的计算方法。原来按照每一研发项目分别计算“其他相关费用”限额，《公告》改为统一计算所有研发项目“其他相关费用”限额，简化了计算方法，允许多个项目“其他相关费用”限额调剂使用，总体上提高了可加计扣除的金额。</p><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\"><span style=\"font-weight: bold;\">三、今年10月申报期，企业享受研发费用加计扣除优惠需准备哪些资料？</span></p><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\">答：企业享受此项优惠实行“真实发生、自行判别、申报享受、相关资料留存备查”方式，依据实际发生的研发费用支出，按税收政策规定在预缴申报表中直接填写前三季度的加计扣除金额，准备前三季度的研发支出辅助账和《研发费用加计扣除优惠明细表》（A107012）等留存备查。</p><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\"><span style=\"font-weight: bold;\">四、如果企业在今年10月份申报期没有享受研发费用加计扣除，以后还可以享受吗？</span></p><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\">答：企业在10月份预缴申报期没有选择享受优惠的，可以在明年5月底前办理汇算清缴时享受。</p><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\"><span style=\"font-weight: bold;\">五、与2015版研发支出辅助账样式相比，2021版研发支出辅助账样式在哪些方面做了优化简化？</span></p><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\">答：与2015版研发支出辅助账样式相比，2021版研发支出辅助账样式主要在以下方面进行了优化简化：</p><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\">一是简并辅助账样式。2015版研发支出辅助账样式包括自主研发、委托研发、合作研发、集中研发等4类辅助账和辅助账汇总表样式，共“4张辅助账+1张汇总表”。2021版研发支出辅助账将4类辅助账样式合并为一类，共“1张辅助账＋1张汇总表”，总体上减少辅助账样式的数量。</p><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\">二是精简辅助账信息。2015版研发支出辅助账样式要求填写人员人工等六大类费用的各项明细信息，并要求填报“借方金额”“贷方金额”等会计信息。2021版研发支出辅助账样式仅要求企业填写人员人工等六大类费用合计，不再填写具体明细费用，同时删除了部分会计信息，减少了企业填写工作量。</p><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\">三是调整优化操作口径。2015版研发支出辅助账样式未体现2015年之后的政策变化情况，如未明确委托境外研发费用的填写要求，企业需自行调整样式或分析填报。2021版研发支出辅助账样式，充分考虑了税收政策的调整情况，增加了委托境外研发的相关列次，体现其他相关费用限额的计算方法的调整。《公告》还对填写口径进行了详细说明，便于纳税人准确归集核算。</p><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\"><span style=\"font-weight: bold;\">六、《公告》实施以后，2015版研发支出辅助账样式还可以继续使用吗？</span></p><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\">答：研发支出辅助账样式的定位是为企业享受加计扣除政策提供一个参照使用的样本，不强制执行。因此，2021版研发支出辅助账样式发布后，2015版研发支出辅助账样式继续有效。纳税人既可以选择使用2021版研发支出辅助账样式，也可以继续选择2015版研发支出辅助账样式。</p><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\">需要说明，企业继续使用2015版研发支出辅助账样式的，可以参考2021版研发支出辅助账样式对委托境外研发费用、其他相关费用限额的计算公式等进行相应调整。</p><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\"><span style=\"font-weight: bold;\">七、企业可以自行设计辅助账样式吗？</span></p><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\">答：纳税人可以自行设计辅助账样式。为保证企业准确归集可加计扣除的研发费用，且与《研发费用加计扣除优惠明细表》（A107012）的数据项相匹配，企业自行设计的辅助账样式，应当至少包括2021版研发支出辅助账样式所列数据项，且逻辑关系一致。</p><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\"><span style=\"font-weight: bold;\">八、为什么要调整其他相关费用限额计算方法，调整后对企业有哪些好处？</span></p><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\">答：按现行政策规定，其他相关费用采取限额管理方式，不得超过可加计扣除研发费用总额的10%。97号公告明确按每一项目分别计算其他相关费用限额，对于有多个研发项目的企业，其有的研发项目其他相关费用占比不到10%，有的超过10%，不同研发项目的限额不能调剂使用。为进一步减轻企业负担、方便计算，让企业更多地享受优惠，《公告》将其他相关费用限额的计算方法调整为按全部项目统一计算，不再分项目计算。</p><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\">举例：假设某公司2021年度有A和B两个研发项目。项目A人员人工等五项费用之和为90万元，其他相关费用为12万元；项目B人员人工等五项费用之和为100万元，其他相关费用为8万元。</p><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\">（一）按照97号公告的计算方法</p><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\">项目A的其他相关费用限额为10万元[90*10%/(1-10%)]，按照孰小原则，可加计扣除的其他相关费用为10万元；项目B的其他相关费用限额为11.11万元[100*10%/(1-10%)]，按照孰小原则，可加计扣除的其他相关费用为8万元。两个项目可加计扣除的其他相关费用合计为18万元。</p><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\">（二）按照《公告》明确的计算方法</p><p align=\"\" style=\"margin-top: 0px; margin-bottom: 0px; text-indent: 2em; text-align: justify;\">两个项目的其他相关费用限额为21.11万元[(90＋100)*10%/(1-10%)]，可加计扣除的其他相关费用为20万元（12＋8），大于18万元，且仅需计算一次，减轻了工作量。</p><p style=\"margin-top: 0px; margin-bottom: 0px;\"></p></div><div style=\"margin: 20px auto; font-size: medium; width: 1000px; color: rgb(0, 0, 0); font-family: &quot;Microsoft YaHei&quot;;\"><ul style=\"margin-top: 0px; margin-bottom: 0px; margin-left: 0px; list-style: none;\"><p style=\"margin-top: 0px; margin-bottom: 0px; font-weight: bold; line-height: 36px;\">相关链接：</p><li style=\"list-style-type: disc;\"><a href=\"http://beijing.chinatax.gov.cn/bjswj/sszc/zxwj/202109/be43c209c38e466ea1e01ceead33a748.shtml\" target=\"_blank\" style=\"cursor: pointer;\">国家税务总局关于进一步落实研发费用加计扣除政策有关问题的公告</a></li></ul></div>"));
    }
}
