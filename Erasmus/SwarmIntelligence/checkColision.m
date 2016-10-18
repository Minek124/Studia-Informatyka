function ret = checkColision(x,y)
global map;
global boid_size;
global map_size;

ret = true;
for i = (x - boid_size):(boid_size + x)
    for j = (y - boid_size):(boid_size+y)
        if(i>0 && i<= map_size && j>0 && j<= map_size)
            if( ((i-x)*(i-x))+((j-y)*(j-y)) <= (boid_size*boid_size) )
                if(map(i,j) == 1)
                    ret = false;
                    return;
                end
            end
        else
            ret = false;
            return;
        end
    end
end

end

