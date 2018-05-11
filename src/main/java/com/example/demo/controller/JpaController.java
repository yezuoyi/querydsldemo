package com.example.demo.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entity.Customer;
import com.example.demo.entity.GoodInfoBean;
import com.example.demo.entity.QCustomer;
import com.example.demo.entity.QGoodInfoBean;
import com.example.demo.entity.QGoodTypeBean;
import com.example.demo.entity.QUser;
import com.example.demo.entity.User;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.UserRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Controller
public class JpaController {
	// private final Logger logger = Logger.getLogger(JpaController.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private CustomerRepository cr;

	@Autowired
	private UserRepository ur;

	private JPAQueryFactory queryFactory;

	@PostConstruct
	public void init() {
		queryFactory = new JPAQueryFactory(entityManager);
	}

	@RequestMapping("/getBob")
	@ResponseBody
	public Customer getBob() {
		QCustomer customer = QCustomer.customer;
		Customer bob = queryFactory.selectFrom(customer).where(customer.firstName.eq("Bob")).fetchOne();
		return bob;
	}

	@RequestMapping("/getByName")
	@ResponseBody
	public Customer getCustomerByName(final String name) {
		QCustomer customer = QCustomer.customer;
		Customer bob = queryFactory.selectFrom(customer).where(customer.firstName.eq(name)).fetchOne();
		return bob;
	}

	@RequestMapping("/getAll")
	@ResponseBody
	public List<Customer> getAll() {
		return cr.findAll();
	}

	@RequestMapping("/getOrderAll")
	@ResponseBody
	public List<Customer> getOrderAll() {
		QCustomer customer = QCustomer.customer;
		return queryFactory.selectFrom(customer).orderBy(customer.lastName.asc(), customer.firstName.desc()).fetch();
	}

	@RequestMapping("/getLastName")
	@ResponseBody
	public List<String> getLastName() {
		QCustomer customer = QCustomer.customer;
		return queryFactory.select(customer.lastName).from(customer).groupBy(customer.lastName).fetch();
	}

	@RequestMapping("/findById")
	@ResponseBody
	public Object getCumtomerById(Long id) {
		return cr.findById(id);
	}

	@RequestMapping("/findName")
	@ResponseBody
	public User findUserByUserName(final String userName) {
		/**
		 * 该例是使用spring data QueryDSL实现
		 */
		QUser quser = QUser.user;
		Predicate predicate = quser.name.eq(userName);// 根据用户名，查询user表
		return ur.findOne(predicate).get();
	}

	/**
	 * Details：单表多条件查询
	 */
	@RequestMapping("/findNameAddr")
	@ResponseBody
	public User findOneByUserNameAndAddress(final String userName, final String address) {
		QUser quser = QUser.user;
		return queryFactory.select(quser).from(quser) // 上面两句代码等价与selectFrom
				.where(quser.name.eq(userName).and(quser.address.eq(address)))// 这句代码等同于where(quser.name.eq(userName),
																				// quser.address.eq(address))
				.fetchOne();
	}

	/**
	 * Details：将查询结果排序
	 */
	@RequestMapping("/findUserAndOrder")
	@ResponseBody
	public List<User> findUserAndOrder() {
		QUser quser = QUser.user;
		return queryFactory.selectFrom(quser).orderBy(quser.id.desc()).fetch();
	}

	/**
	 * Details：分页查询单表
	 */
	public Page<User> findAllAndPager(final int offset, final int pageSize) {
		Predicate predicate = QUser.user.id.lt(10);
		Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
		PageRequest pr = new PageRequest(offset, pageSize, sort);
		return ur.findAll(predicate, pr);
	}

	/**
	 * Details：删除用户
	 */
	public long deleteUser(String userName) {
		QUser quser = QUser.user;
		return queryFactory.delete(quser).where(quser.name.eq(userName)).execute();
	}

	/**
	 * Details：更新记录
	 */
	public long updateUser(final User u, final String userName) {
		QUser quser = QUser.user;
		return queryFactory.update(quser).where(quser.name.eq(userName)).set(quser.name, u.getName())
				.set(quser.age, u.getAge()).set(quser.address, u.getAddress()).execute();
	}

	/**
	 * Details：使用原生Query
	 */
	public User findOneUserByOriginalSql(final String userName) {
		QUser quser = QUser.user;
		Query query = queryFactory.selectFrom(quser).where(quser.name.eq(userName)).createQuery();
		return (User) query.getSingleResult();
	}
	
	/**
	 * 这种方式联repository
	 * @param typeId
	 * @return
	 */
	@RequestMapping(value = "/selectByType")
	@ResponseBody
    public List<GoodInfoBean> selectByType
            (
                    @RequestParam(value = "typeId") Long typeId //类型编号
            )
    {
        //商品查询实体
        QGoodInfoBean _Q_good = QGoodInfoBean.goodInfoBean;
        //商品类型查询实体
        QGoodTypeBean _Q_good_type = QGoodTypeBean.goodTypeBean;
        return
                queryFactory
                .select(_Q_good)
                .from(_Q_good,_Q_good_type)
                .where(
                        //为两个实体关联查询
                        _Q_good.typeId.eq(_Q_good_type.id)
                        .and(
                                //查询指定typeid的商品
                                _Q_good_type.id.eq(typeId)
                        )
                )
                //根据排序字段倒序
                .orderBy(_Q_good.order.desc())
                //执行查询
                .fetch();
    }
}



