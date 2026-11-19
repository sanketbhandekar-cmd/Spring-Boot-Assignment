package com.company.leavemanagement.repository;

import com.company.leavemanagement.model.LeaveRequest;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class LeaveRepository {

    private final Map<Long, LeaveRequest> leaveStore = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public LeaveRequest save(LeaveRequest leaveRequest) {
        if (leaveRequest.getId() == null) {
            leaveRequest.setId(idCounter.getAndIncrement());
        }
        leaveStore.put(leaveRequest.getId(), leaveRequest);
        return leaveRequest;
    }

    public Optional<LeaveRequest> findById(Long id) {
        return Optional.ofNullable(leaveStore.get(id));
    }

    public List<LeaveRequest> findAll() {
        return new ArrayList<>(leaveStore.values());
    }

    public boolean deleteById(Long id) {
        return leaveStore.remove(id) != null;
    }

    public boolean existsById(Long id) {
        return leaveStore.containsKey(id);
    }
}
