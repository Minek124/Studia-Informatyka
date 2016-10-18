function setBoid(x,y,v)
global map;
global boid_size;
global map_size;

for i = (x - boid_size):(boid_size + x)
    for j = (y - boid_size):(boid_size+y)
        if(i>0 && i<= map_size && j>0 && j<= map_size)
            if( ((i-x)*(i-x))+((j-y)*(j-y)) <= (boid_size*boid_size) )
                    map(i,j)=v;
            end
        end
    end
end
end

