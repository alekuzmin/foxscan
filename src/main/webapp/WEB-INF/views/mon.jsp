<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link href="${pageContext.request.contextPath}/style/stat.css" rel="stylesheet" type="text/css">

</head>
<body>
</body>
<script type="text/javascript">


    var svgNS = 'http://www.w3.org/2000/svg';
    var svg = document.createElementNS(svgNS,'svg');

    function formNet( width, height, difX, difY, countX, countY){

        var stepX = width/countX;
        var stepY = height/countY;

        var axisX = document.createElementNS(svgNS,'line');
        axisX.setAttributeNS(null,'x1', difX);
        axisX.setAttributeNS(null,'y1', height + difY);
        axisX.setAttributeNS(null,'x2', width + difX);
        axisX.setAttributeNS(null,'y2', height + difY);
        axisX.setAttributeNS(null,'class', 'axis');
        var axisY = document.createElementNS(svgNS,'line');
        axisY.setAttributeNS(null,'x1', difX);
        axisY.setAttributeNS(null,'y1', difY);
        axisY.setAttributeNS(null,'x2', difX);
        axisY.setAttributeNS(null,'y2', height+difY);
        axisY.setAttributeNS(null,'class', 'axis');

        for (var i = 0; i < countX; i++ ){
            var line = document.createElementNS(svgNS,'line');
            line.setAttributeNS(null,'x1', difX+stepX+stepX*i);
            line.setAttributeNS(null,'y1', difY);
            line.setAttributeNS(null,'x2', difX+stepX+stepX*i);
            line.setAttributeNS(null,'y2', height+difY);
            svg.appendChild(line);
        }
        for (var i = 0; i <= countX; i++ ){
            var textX = document.createElementNS(svgNS,'text');
            textX.setAttributeNS(null,'class','axis-textX');
            textX.setAttributeNS(null,'x',difX+stepX*i-stepX/2+10);
            textX.setAttributeNS(null,'y',difY+height+20);
            textX.textContent = getFormatHour(i)+':00';
            svg.appendChild(textX);
        }

        for (var i = 0; i < countY; i++ ){
            var line = document.createElementNS(svgNS,'line');
            line.setAttributeNS(null,'x1', difX);
            line.setAttributeNS(null,'y1', difY+stepY*i);
            line.setAttributeNS(null,'x2', width+difX);
            line.setAttributeNS(null,'y2', difY+stepY*i);
            svg.appendChild(line);
            var textY = document.createElementNS(svgNS,'text');
            textY.setAttributeNS(null,'class','axis-textY');
            textY.setAttributeNS(null,'x',difX-60);
            textY.setAttributeNS(null,'y',difY+stepY/2+5+stepY*i);
            textY.textContent = getDateText(i);
            svg.appendChild(textY);
        }
        for (var i = 0; i < countY; i++ ){

            if(!isWeekEnd(i)){
                formRect(difX+stepX*8, difX+stepX*9.5, difY, stepY,  i);
                formRect(difX+stepX*12.5, difX+stepX*14, difY, stepY,  i);
                if(isFtiday(i)){
                    formRect(difX+stepX*16, difX+stepX*18, difY, stepY,  i);
                } else {
                    formRect(difX+stepX*17, difX+stepX*19, difY, stepY,  i);
                }

            }
        }
        formLegendBox('tech_l', difX+20, difY-50);
        formLegendText(' - тех окно полигона КИТ', difX+40, difY-40);
        formLegendBox('standIn_l', difX+200, difY-50);
        formLegendText(' - режим standIn', difX+220, difY-40);
        formLegendBox('disconnect_l', difX+320, difY-50);
        formLegendText(' - disconnect', difX+340, difY-40);

        svg.appendChild(axisX);
        svg.appendChild(axisY);

        <% out.print(request.getAttribute("statDataAll"));%>

        function formStatusRect (statusData, dayLineNumber, statusName){
            for (var i = 0; i < statusData.length; i++ ){
                var rect = document.createElementNS(svgNS,'rect');
                rect.setAttributeNS(null,'class', statusName);
                rect.setAttributeNS(null,'x', difX+stepX*statusData[i][0]);
                rect.setAttributeNS(null,'y', difY+stepY*dayLineNumber);
                rect.setAttributeNS(null,'width', (difX+stepX*statusData[i][1])-(difX+stepX*statusData[i][0]));
                rect.setAttributeNS(null,'height', stepY);
                var text = document.createElementNS(svgNS,'text');
                text.setAttributeNS(null,'class','label');
                text.setAttributeNS(null,'x',difX+stepX*statusData[i][0]);
                text.setAttributeNS(null,'y',difY+stepY*dayLineNumber-10);
                text.textContent = statusData[i][2];
                svg.appendChild(rect);
                svg.appendChild(text);
            }
        }

    }

    function formLegendBox(className, x, y){
        var rect = document.createElementNS(svgNS,'rect');
        rect.setAttributeNS(null,'class', className);
        rect.setAttributeNS(null,'x', x);
        rect.setAttributeNS(null,'y', y);
        rect.setAttributeNS(null,'width', 15);
        rect.setAttributeNS(null,'height', 15);
        svg.appendChild(rect);
    }

    function formRect(start, end, difY, stepY, i){
        var rect = document.createElementNS(svgNS,'rect');
        rect.setAttributeNS(null,'class', 'tech');
        rect.setAttributeNS(null,'x', start);
        rect.setAttributeNS(null,'y', difY+stepY*i);
        rect.setAttributeNS(null,'width', end-start);
        rect.setAttributeNS(null,'height', stepY);
        svg.appendChild(rect);
    }
    function formLegendText(text, x, y){
        var textL = document.createElementNS(svgNS,'text');
        textL.setAttributeNS(null,'class','axis-textX');
        textL.setAttributeNS(null,'x',x);
        textL.setAttributeNS(null,'y',y);
        textL.textContent = text;
        svg.appendChild(textL);
    }

    function isWeekEnd(day_dif){
        var d = new Date(Date.now() - 24*60*60*1000*day_dif);
        if(d.getDay() == 0||d.getDay() == 6){
            return true;
        } else {
            return false;
        }
    }

    function isFtiday(day_dif){
        var d = new Date(Date.now() - 24*60*60*1000*day_dif);
        if(d.getDay() == 5){
            return true;
        } else {
            return false;
        }
    }

    function getDateText(day_dif){
        var d = new Date(Date.now() - 24*60*60*1000*day_dif);
        var day = (d.getDate().toString().length == 1) ? ("0" + d.getDate()) : d.getDate();
        var month = ((d.getMonth()+1).toString().length == 1) ? ("0" + (d.getMonth()+1)) : d.getMonth()+1;
        return day + '.' + month;
    }

    function getFormatHour(h){
        var f_h = (h.toString().length == 1) ? ("0" + h) : h;
        return f_h;
    }

    formNet(1000, 400, 250, 150, 24, 7);
    document.body.appendChild(svg);
</script>
</html>
