var graph = {};

graph.data = [
    {"name": "CampaignService", "type": "service"},
    {"name": "ConfigService", "type": "service"},
    {"name": "InventoryService", "type": "service"},
    {"name": "SSP_Service", "type": "service"}];


graph.nodeValues = d3.values(graph.data);
//debugger;

graph.force = d3.layout.force()
    .nodes(graph.nodeValues)
    .links(graph.links)
    .linkStrength(function(d) { return d.strength; })
    .size([graph.width, graph.height])
    .linkDistance(config.graph.linkDistance)
    .charge(config.graph.charge)
    .on('tick', tick);

graph.svg = d3.select('#graph').append('svg')
    .attr('width' , graph.width  + graph.margin.left + graph.margin.right)
    .attr('height', graph.height + graph.margin.top  + graph.margin.bottom)
    .append('g')
    .attr('transform', 'translate(' + graph.margin.left + ',' + graph.margin.top + ')');

log("***");
