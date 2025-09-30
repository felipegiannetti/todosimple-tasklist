package com.lucasangeloSpring.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lucasangeloSpring.models.Task;
import com.lucasangeloSpring.models.User;
import com.lucasangeloSpring.models.enums.ProfileEnum;
import com.lucasangeloSpring.models.projection.TaskProjection;
import com.lucasangeloSpring.repositories.TaskRepository;
import com.lucasangeloSpring.security.UserSpringSecurity;
import com.lucasangeloSpring.services.exceptions.AuthorizationException;
import com.lucasangeloSpring.services.exceptions.DataBindingViolationException;
import com.lucasangeloSpring.services.exceptions.ObjectNotFoundException;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task findById(Long id) {
        Task task = this.taskRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Task nao encontrada! ID: " + id));

        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if(userSpringSecurity == null || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) || !userHasTask(userSpringSecurity, task)) {
            throw new AuthorizationException("Acesso negado");
        }
        return task;
    }

    public List<TaskProjection> findAllByUser() {
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if(userSpringSecurity == null) {
            throw new AuthorizationException("Acesso negado");
        }

        List<TaskProjection> tasks = this.taskRepository.findByUser_Id(userSpringSecurity.getId());
        return tasks;
    }

    @Transactional
    public Task create(Task obj){
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if(userSpringSecurity == null) {
            throw new AuthorizationException("Acesso negado");
        }

        User user = this.userService.findById(userSpringSecurity.getId());
        obj.setId(null);
        obj.setUser(user);
        obj = this.taskRepository.save(obj);
        return obj;
    }

    @Transactional
    public Task update(Task obj) {
        Task newObj = this.findById(obj.getId());
        newObj.setDescription(obj.getDescription());
        return this.taskRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id);
        
        try {
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("Erro ao deletar a tarefa! ID: " + id);
        }
    }


    private boolean userHasTask(UserSpringSecurity userSpringSecurity, Task task) {
        return task.getUser().getId().equals(userSpringSecurity.getId());
    }

    
    public List<Task> findAll() {
        return this.taskRepository.findAll();
    }
}
