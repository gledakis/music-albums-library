package eu.paasport.samples.music.web.controllers;

import java.util.Random;
import javax.validation.Valid;
import eu.paasport.samples.music.domain.Album;
import eu.paasport.samples.music.repositories.AlbumRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(value = "/albumsnodelays")
public class AlbumControllerNoDelays {
    private static final Logger logger = LoggerFactory.getLogger(AlbumControllerNoDelays.class);
    private AlbumRepository repository;

    @Autowired
    public AlbumControllerNoDelays(AlbumRepository repository) {
        this.repository = repository;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Album> albums() {
        
        //Random r = new Random();
        //long tstart = System.currentTimeMillis();
        //doWork(r.nextInt(10000 + r.nextInt(60000)));
        //long telapsed = System.currentTimeMillis() - tstart;        
        return repository.findAll();
        
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT)
    public Album add(@RequestBody @Valid Album album) {
        logger.info("Adding album " + album.getId());
        return repository.save(album);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public Album update(@RequestBody @Valid Album album) {
        logger.info("Updating album " + album.getId());
        return repository.save(album);
    }

    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Album getById(@PathVariable String id) {
        logger.info("Getting album " + id);
        return repository.findOne(id);
    }

    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteById(@PathVariable String id) {
        logger.info("Deleting album " + id);
        repository.delete(id);
    }
    
    
    private void doWork(int t) {
            for (int i = 0; i < t*100000; i++)
                    Math.tan(Math.atan(Math.tan(Math.atan(Math.tan(Math.atan(Math.tan(Math.atan(Math.PI))))))));
    }
}