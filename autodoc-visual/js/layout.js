function forceLayout(containerSelector, selector, layoutConfig, links) {
    select = d3.selectAll(selector);
    vis = d3.select(containerSelector);
    /* container */
    var
        w = vis._groups[0][0].offsetWidth,
        h = vis._groups[0][0].offsetHeight;

    var data = select._groups[0];

    var node = select
        .data(data);

    // initial positioning
    // setup columns
    var grid = layoutConfig.grid;
    var padding_x = 0;
    var padding_y = 0;
    if (layoutConfig.padding) {
        padding_x = layoutConfig.padding.x;
        padding_y = layoutConfig.padding.y
    }
    if (grid) {
        var columnWidth = new Array(100).fill(0);
        var i, j;

        for (i = 0; i < grid.length; i++) {
            for (j = 0; j < grid[i].length; j++) {
                var colNodes = d3.selectAll(grid[i][j])._groups[0];
                colNodes.forEach(function(n){
                    if (columnWidth[j] < n.offsetWidth) {
                        columnWidth[j] = n.offsetWidth
                    }
                })
            }
        }
        columnWidth = columnWidth.map(function(x) {return x + padding_x});

        for (i = 0; i < grid.length; i++) {
            for (j = 0; j < grid[i].length; j++) {
                var nodes = d3.selectAll(grid[i][j]);
                nodes.style("left", function(d, ic, gr) {
                    var offsetX = padding_x + columnWidth.slice(0, j).reduce( (acc, cur) => acc + cur, 0);
                    this.x = offsetX;
                    return offsetX + 'px'
                });
                nodes.style("top", function (n, ic) {
                    var offsetY = ic * (this.offsetHeight + padding_y);
                    this.y = offsetY;
                    return offsetY + 'px'
                })
            }
        }

    }


    var simulation = d3.forceSimulation()
        //.size([w, h]) // gravity field's size(x, y)
        .nodes(data);
        /*.friction(0.1) // 1 = frictionless
        .charge(100)
        .theta(.8)
        .gravity(0)*/
        //.links(links)
        /*.start();*/

    simulation
        .force("charge_force", d3.forceManyBody())
        .force("collide_force", d3.forceCollide(150));

    var drag = d3.drag()
        .on("drag", onDrag);

    node.call(drag);

    function onDrag() {
        d3.select(this).attr("x", d3.event.x).attr("y", d3.event.y);
        d3.select(this).style("left", parseInt(this.style.left) + d3.event.dx + "px")
        d3.select(this).style("top", parseInt(this.style.top) + d3.event.dy + "px")
    }


    /*vis.style("opacity", 1e-6)
        .transition()
        .duration(1000)
        .style("opacity", 1);*/



    simulation.on("tick", function() {
        // Get items coords (then whole force's maths managed by D3)
        //force.alpha(-1);

        var q = d3.quadtree(data),
            n = data.length;

        for (var i = 0; i < n; i++)
            q.visit(collide(data[i]));


        var k = .6 * this.alpha();
        data.forEach(function (o, i) {
            // outer bounds
            if (o.x > 800) o.x += (680 - o.x) * k;
            if (o.y > 500) o.y += (420 - o.y) * k;
            if (o.x < 0) o.x = 0;
            if (o.y < 0) o.y = 0;

            // grid
            o.x += (nearest(o.x, 40) - o.x) * k;
            o.y += (nearest(o.y, 20) - o.y) * k;
        });


        node.style("left", function (d, i) {
            return d.x + 'px';
        })
            .style("top", function (d) {
                return d.y + 'px';
            })
    });
}


var nearest = function (n, range) {
    return Math.round(n / range) * range;
};


function collide_v3(node) {
    return function(quad, x1, y1, x2, y2) {
        var updated = false;
        if (quad.point && (quad.point !== node)) {

            var x = node.x - quad.point.x,
                y = node.y - quad.point.y,
                xSpacing = (quad.point.offsetWidth + node.offsetWidth) / 2 + 40,
                ySpacing = (y > 0 ? quad.point.offsetHeight : node.offsetHeight) + 20,
                absX = Math.abs(x),
                absY = Math.abs(y),
                l,
                lx,
                ly;

            if (absX < xSpacing && absY < ySpacing) {
                l = Math.sqrt(x * x + y * y);

                lx = (absX - xSpacing) / l;
                ly = (absY - ySpacing) / l;

                // the one that's barely within the bounds probably triggered the collision
                if (Math.abs(lx) > Math.abs(ly)) {
                    lx = 0;
                } else {
                    ly = 0;
                }

                node.x -= x *= lx;
                node.y -= y *= ly;
                quad.point.x += x;
                quad.point.y += y;

                updated = true;
            }
        }
        return updated;
    };
}

function collide(radius) {
    var nodes,
        radii,
        strength = 1,
        iterations = 1;

    if (typeof radius !== "function") radius = constant$7(radius == null ? 1 : +radius);

    function force() {
        var i, n = nodes.length,
            tree,
            node,
            xi,
            yi,
            ri,
            ri2;

        for (var k = 0; k < iterations; ++k) {
            tree = quadtree(nodes, x$1, y$1).visitAfter(prepare);
            for (i = 0; i < n; ++i) {
                node = nodes[i];
                ri = radii[node.index], ri2 = ri * ri;
                xi = node.x + node.vx;
                yi = node.y + node.vy;
                tree.visit(apply);
            }
        }

        function apply(quad, x0, y0, x1, y1) {
            var data = quad.data, rj = quad.r, r = ri + rj;
            if (data) {
                if (data.index > node.index) {
                    var x = xi - data.x - data.vx,
                        y = yi - data.y - data.vy,
                        l = x * x + y * y;
                    if (l < r * r) {
                        if (x === 0) x = jiggle(), l += x * x;
                        if (y === 0) y = jiggle(), l += y * y;
                        l = (r - (l = Math.sqrt(l))) / l * strength;
                        node.vx += (x *= l) * (r = (rj *= rj) / (ri2 + rj));
                        node.vy += (y *= l) * r;
                        data.vx -= x * (r = 1 - r);
                        data.vy -= y * r;
                    }
                }
                return;
            }
            return x0 > xi + r || x1 < xi - r || y0 > yi + r || y1 < yi - r;
        }
    }

    function prepare(quad) {
        if (quad.data) return quad.r = radii[quad.data.index];
        for (var i = quad.r = 0; i < 4; ++i) {
            if (quad[i] && quad[i].r > quad.r) {
                quad.r = quad[i].r;
            }
        }
    }

    function initialize() {
        if (!nodes) return;
        var i, n = nodes.length, node;
        radii = new Array(n);
        for (i = 0; i < n; ++i) node = nodes[i], radii[node.index] = +radius(node, i, nodes);
    }

    force.initialize = function(_) {
        nodes = _;
        initialize();
    };

    force.iterations = function(_) {
        return arguments.length ? (iterations = +_, force) : iterations;
    };

    force.strength = function(_) {
        return arguments.length ? (strength = +_, force) : strength;
    };

    force.radius = function(_) {
        return arguments.length ? (radius = typeof _ === "function" ? _ : constant$7(+_), initialize(), force) : radius;
    };

    return force;
};

var constant$7 = function(x) {
    return function() {
        return x;
    };
};
