// from https://www.beyondjava.net/how-to-connect-html-elements-with-an-arrow-using-svg

// ------------------------------ this part from stropa ------------------------------- //

class Connector {
    constructor(left_edge, right_edge, path) {
        this.left_edge = left_edge;
        this.right_edge = right_edge;
    }
}

class Connection {
    constructor(nearEdge, remoteEdge, path) {
        this.nearEdge = nearEdge;
        this.remoteEdge = remoteEdge;
        this.path = path;
    }
}

connectors = [];

// ------------------------------ end of extension part ------------------------------- //

function createSVG() {
    var svg = document.getElementById("svg-canvas");
    if (null == svg) {
        svg = document.createElementNS("http://www.w3.org/2000/svg",
            "svg");
        svg.setAttribute('id', 'svg-canvas');
        svg.setAttribute('style', 'position:absolute;top:0px;left:0px');
        svg.setAttribute('width', document.body.clientWidth);
        svg.setAttribute('height', Math.max( window.innerHeight, document.body.clientHeight ));
        svg.setAttribute('pointer-events', 'none');
        svg.setAttributeNS("http://www.w3.org/2000/xmlns/",
            "xmlns:xlink",
            "http://www.w3.org/1999/xlink");
        document.body.appendChild(svg);
    }
    return svg;
}

function drawCircle(x, y, radius, color) {
    var svg = createSVG();
    var shape = document.createElementNS("http://www.w3.org/2000/svg", "circle");
    shape.setAttributeNS(null, "cx", x);
    shape.setAttributeNS(null, "cy", y);
    shape.setAttributeNS(null, "r", radius);
    shape.setAttributeNS(null, "fill", color);
    svg.appendChild(shape);
    return shape;
}


function findAbsolutePosition(htmlElement) {
    var x = htmlElement.offsetLeft;
    var y = htmlElement.offsetTop;
    for (var x = 0, y = 0, el = htmlElement; el != null;  el = el.offsetParent) {
        x += el.offsetLeft;
        y += el.offsetTop;
    }
    return {
        "x": x,
        "y": y
    };
}


function bezierCurveData(x1, y1, x2, y2, tension) {
    var delta = (x2 - x1) * tension;
    var hx1 = x1 + delta;
    var hy1 = y1;
    var hx2 = x2 - delta;
    var hy2 = y2;
    var path = "M " + x1 + " " + y1 +
        " C " + hx1 + " " + hy1
        + " " + hx2 + " " + hy2
        + " " + x2 + " " + y2;
    return path;
}

function drawCurvedLine(x1, y1, x2, y2, color, tension) {
    var svg = createSVG();
    var shape = document.createElementNS("http://www.w3.org/2000/svg",
        "path");

    var path = bezierCurveData(x1, y1, x2, y2, tension);
    shape.setAttributeNS(null, "d", path);
    shape.setAttributeNS(null, "fill", "none");
    shape.setAttributeNS(null, "stroke", color);
    svg.appendChild(shape);
    return shape
}


function connectDivs(leftId, rightId, color, tension) {
    var right = document.getElementById(leftId);
    var left = document.getElementById(rightId);

    var leftPos = findAbsolutePosition(left);
    var x1 = leftPos.x;
    var y1 = leftPos.y;
    x1 += left.offsetWidth;
    y1 += (left.offsetHeight / 2);

    var rightPos = findAbsolutePosition(right);
    var x2 = rightPos.x;
    var y2 = rightPos.y;
    y2 += (right.offsetHeight / 2);

    var width = x2 - x1;
    var height = y2 - y1;

    left_edge = drawCircle(x1, y1, 3, color);
    right_edge = drawCircle(x2, y2, 3, color);
    conn_path = drawCurvedLine(x1, y1, x2, y2, color, tension);

    connectors.push(new Connector(left_edge, right_edge, conn_path));
    if(!right.connections) {
        right.connections = []
    }
    if(!left.connections) {
        left.connections = []
    }
    right.connections.push(new Connection(right_edge, left_edge, conn_path));
    left.connections.push(new Connection(left_edge, right_edge, conn_path))
}
